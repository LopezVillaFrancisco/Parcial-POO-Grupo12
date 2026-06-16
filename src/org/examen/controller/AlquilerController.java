package org.examen.controller;

import org.examen.dto.AlquilerDTO;
import org.examen.dto.DetalleAlquilerDTO;
import org.examen.dto.PagoDTO;
import org.examen.model.Alquiler;
import org.examen.model.AlquilerComun;
import org.examen.model.AlquilerCorporativo;
import org.examen.model.AlquilerMasivo;
import org.examen.model.Cliente;
import org.examen.model.DetalleAlquiler;
import org.examen.model.Equipo;
import org.examen.model.Pago;
import org.examen.model.enums.EstadoAlquiler;
import org.examen.model.enums.MedioPago;
import org.examen.model.enums.TipoEntidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador Singleton de alquileres. Mantiene en memoria la lista de
 * alquileres y coordina los casos de uso "Solicitar alquiler" (UC3),
 * "Finalizar alquiler y calcular saldo" (UC4), la confirmacion y cancelacion
 * de alquileres y las consultas del enunciado.
 *
 * Se comunica con los demas controladores exclusivamente a traves de sus
 * instancias Singleton (ClienteController, EquipoController, HistorialController).
 */
public class AlquilerController {

    private static final int HORAS_MINIMAS_REINTEGRO = 72;

    private static AlquilerController instance;

    private final List<Alquiler> listaAlquileres;

    // Parametrizacion general de recargos (antes en la clase Sistema).
    private double recargoComun = 0;
    private double recargoCorporativo = 10;
    private double recargoMasivo = 20;

    private AlquilerController() {
        this.listaAlquileres = new ArrayList<>();
    }

    public static AlquilerController getInstance() {
        if (instance == null) {
            instance = new AlquilerController();
        }
        return instance;
    }

    /**
     * UC3 - Solicitar alquiler.
     * Busca el cliente, crea el alquiler del tipo indicado, verifica el stock de
     * cada equipo solicitado, arma los detalles, reserva el stock, deja el
     * alquiler en estado INGRESADO y registra el alta en la auditoria.
     */
    public AlquilerDTO solicitarAlquiler(AlquilerDTO dto, String usuario) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del alquiler son obligatorios.");
        }

        Cliente cliente = ClienteController.getInstance().buscarPorDniCuit(dto.getDniCuitCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el DNI/CUIT " + dto.getDniCuitCliente());
        }
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El alquiler debe incluir al menos un equipo.");
        }

        Alquiler alquiler = instanciarPorTipo(dto.getTipoAlquiler(), cliente,
                dto.getFechaEvento(), dto.getCantidadDias());

        // Recorrer items solicitados: validar disponibilidad, armar detalle y reservar stock.
        for (DetalleAlquilerDTO itemDTO : dto.getDetalles()) {
            Equipo equipo = EquipoController.getInstance().buscarPorCodigo(itemDTO.getCodigoEquipo());
            if (equipo == null) {
                throw new IllegalArgumentException("No existe un equipo con el codigo " + itemDTO.getCodigoEquipo());
            }
            if (!equipo.estaDisponible(itemDTO.getCantidad())) {
                throw new IllegalStateException("El equipo " + equipo.getCodigo()
                        + " no esta disponible o no posee stock suficiente.");
            }
            // Disponibilidad real para el periodo solicitado: stock total menos lo
            // comprometido por otros alquileres que se solapan en fechas, menos lo
            // ya pedido en este mismo alquiler.
            int yaSolicitado = alquiler.cantidadReservadaDeEquipo(equipo.getCodigo());
            int disponiblePeriodo = cantidadDisponibleEnPeriodo(
                    equipo, dto.getFechaEvento(), dto.getCantidadDias()) - yaSolicitado;
            if (disponiblePeriodo < itemDTO.getCantidad()) {
                throw new IllegalStateException("El equipo " + equipo.getCodigo()
                        + " no tiene stock disponible para el periodo solicitado.");
            }
            DetalleAlquiler detalle = new DetalleAlquiler(
                    itemDTO.getCantidad(), equipo.getValorDiario(), equipo);
            alquiler.agregarDetalle(detalle);
        }

        alquiler.setEstado(EstadoAlquiler.INGRESADO);
        listaAlquileres.add(alquiler);

        HistorialController.getInstance().registrar(
                "-", EstadoAlquiler.INGRESADO.name(), TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getId()), usuario);

        return toDTO(alquiler);
    }

    /**
     * Confirma un alquiler: registra la senia como un pago confirmado y deja el
     * alquiler en estado CONFIRMADO. Registra los cambios de estado del pago y
     * del alquiler en la auditoria.
     */
    public AlquilerDTO confirmarAlquiler(int idAlquiler, double importeSenia, String medioPago, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null) {
            throw new IllegalArgumentException("No existe un alquiler con el id " + idAlquiler);
        }
        if (alquiler.getEstado() != EstadoAlquiler.INGRESADO) {
            throw new IllegalStateException("Solo se puede confirmar un alquiler en estado INGRESADO.");
        }

        Pago pago = new Pago(new Date(), importeSenia, MedioPago.valueOf(medioPago), usuario);
        pago.confirmar();
        alquiler.registrarPago(pago);
        alquiler.registrarSenia(importeSenia);

        HistorialController.getInstance().registrar(
                "-", pago.getEstado().name(), TipoEntidad.PAGO, String.valueOf(pago.getId()), usuario);

        String estadoAnterior = alquiler.getEstado().name();
        alquiler.confirmar();
        HistorialController.getInstance().registrar(
                estadoAnterior, alquiler.getEstado().name(), TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getId()), usuario);

        return toDTO(alquiler);
    }

    /**
     * Cancela un alquiler confirmado. Libera el stock reservado y aplica la
     * regla de las 72 horas: si la cancelacion se realiza con mas de 72 horas de
     * anticipacion, la senia queda como credito a favor del cliente; en caso
     * contrario, la senia no se reintegra.
     */
    public AlquilerDTO cancelarAlquiler(int idAlquiler, Date fechaCancelacion, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null) {
            throw new IllegalArgumentException("No existe un alquiler con el id " + idAlquiler);
        }

        // Al cancelar, el alquiler deja de ocupar stock: la disponibilidad por
        // periodo se recalcula automaticamente a partir de los alquileres activos.

        int horasAnticipacion = alquiler.calcularHorasAnticipacion(fechaCancelacion);
        if (horasAnticipacion > HORAS_MINIMAS_REINTEGRO && alquiler.getCliente() != null) {
            alquiler.getCliente().agregarCredito(alquiler.getSeniaAbonada());
        }

        String estadoAnterior = alquiler.getEstado().name();
        alquiler.cancelar();
        HistorialController.getInstance().registrar(
                estadoAnterior, alquiler.getEstado().name(), TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getId()), usuario);

        return toDTO(alquiler);
    }

    /**
     * UC4 - Finalizar alquiler y calcular saldo.
     * Libera el stock de los equipos devueltos, calcula el importe total y el
     * importe pendiente considerando recargo y descuento del cliente, deja el
     * alquiler en estado FINALIZADO y devuelve el importe pendiente.
     */
    public double finalizarAlquiler(int idAlquiler, String usuario) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null) {
            throw new IllegalArgumentException("No existe un alquiler con el id " + idAlquiler);
        }

        Cliente cliente = alquiler.getCliente();

        // Al finalizar, los equipos se devuelven y el alquiler deja de ocupar
        // stock para periodos futuros (la disponibilidad se recalcula sola).

        double porcentajeDescuento = cliente != null
                ? cliente.obtenerDescuentoVigente(alquiler.getFechaEvento())
                : 0;

        alquiler.calcularImporteTotal(porcentajeDescuento);
        double importePendiente = alquiler.calcularImportePendiente(porcentajeDescuento);

        String estadoAnterior = alquiler.getEstado().name();
        alquiler.finalizar();
        HistorialController.getInstance().registrar(
                estadoAnterior, alquiler.getEstado().name(), TipoEntidad.ALQUILER,
                String.valueOf(alquiler.getId()), usuario);

        return importePendiente;
    }

    /**
     * Consulta 1 - Total recaudado por alquileres finalizados en un periodo
     * (segun la fecha del evento). Suma el importe total de cada alquiler
     * finalizado dentro del rango.
     */
    public double totalRecaudado(Date fechaDesde, Date fechaHasta) {
        double total = 0;
        for (Alquiler alquiler : listaAlquileres) {
            if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO
                    && estaEnRango(alquiler.getFechaEvento(), fechaDesde, fechaHasta)) {
                total += alquiler.getImporteTotal();
            }
        }
        return total;
    }

    /**
     * Consulta 2 - Alquileres confirmados de un cliente pasado por parametro.
     */
    public List<AlquilerDTO> obtenerAlquileresConfirmados(String dniCuit) {
        List<AlquilerDTO> resultado = new ArrayList<>();
        for (Alquiler alquiler : listaAlquileres) {
            if (alquiler.getEstado() == EstadoAlquiler.CONFIRMADO
                    && alquiler.getCliente() != null
                    && dniCuit.equals(alquiler.getCliente().getDniCuit())) {
                resultado.add(toDTO(alquiler));
            }
        }
        return resultado;
    }

    /**
     * Consulta 3 - Porcentaje neto de recargo/descuento aplicable a un alquiler
     * segun su tipo y el descuento vigente del cliente (recargo - descuento).
     */
    public double obtenerRecargoDescuentoAplicable(int idAlquiler) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        if (alquiler == null) {
            throw new IllegalArgumentException("No existe un alquiler con el id " + idAlquiler);
        }
        double recargo = alquiler.obtenerPorcentajeRecargo();
        double descuento = alquiler.getCliente() != null
                ? alquiler.getCliente().obtenerDescuentoVigente(alquiler.getFechaEvento())
                : 0;
        return recargo - descuento;
    }

    /**
     * Cantidad del equipo indicado comprometida por los alquileres activos cuyo
     * periodo se solapa con el rango [fechaEvento, fechaEvento + cantidadDias].
     */
    public int cantidadComprometida(String codigoEquipo, Date fechaEvento, int cantidadDias) {
        Date desde = fechaEvento;
        Date hasta = calcularFechaFin(fechaEvento, cantidadDias);
        int comprometida = 0;
        for (Alquiler alquiler : listaAlquileres) {
            if (alquiler.ocupaStock() && alquiler.seSolapaConPeriodo(desde, hasta)) {
                comprometida += alquiler.cantidadReservadaDeEquipo(codigoEquipo);
            }
        }
        return comprometida;
    }

    /**
     * Stock del equipo realmente disponible para el periodo solicitado: stock
     * total menos lo comprometido por los alquileres activos que se solapan.
     */
    public int cantidadDisponibleEnPeriodo(Equipo equipo, Date fechaEvento, int cantidadDias) {
        int comprometida = cantidadComprometida(equipo.getCodigo(), fechaEvento, cantidadDias);
        return equipo.getStockDisponible() - comprometida;
    }

    /**
     * Busca el modelo Alquiler por su id. Pensado para la comunicacion entre
     * controladores (devuelve el modelo, no el DTO).
     */
    public Alquiler buscarPorId(int idAlquiler) {
        for (Alquiler alquiler : listaAlquileres) {
            if (alquiler.getId() == idAlquiler) {
                return alquiler;
            }
        }
        return null;
    }

    public AlquilerDTO obtenerAlquiler(int idAlquiler) {
        Alquiler alquiler = buscarPorId(idAlquiler);
        return alquiler != null ? toDTO(alquiler) : null;
    }

    public List<AlquilerDTO> obtenerTodos() {
        List<AlquilerDTO> resultado = new ArrayList<>();
        for (Alquiler alquiler : listaAlquileres) {
            resultado.add(toDTO(alquiler));
        }
        return resultado;
    }

    // Parametrizacion general de recargos por tipo de alquiler.

    public double getRecargoComun() {
        return recargoComun;
    }

    public void setRecargoComun(double recargoComun) {
        this.recargoComun = recargoComun;
    }

    public double getRecargoCorporativo() {
        return recargoCorporativo;
    }

    public void setRecargoCorporativo(double recargoCorporativo) {
        this.recargoCorporativo = recargoCorporativo;
    }

    public double getRecargoMasivo() {
        return recargoMasivo;
    }

    public void setRecargoMasivo(double recargoMasivo) {
        this.recargoMasivo = recargoMasivo;
    }

    private Alquiler instanciarPorTipo(String tipoAlquiler, Cliente cliente,
                                       Date fechaEvento, int cantidadDias) {
        if (tipoAlquiler == null) {
            throw new IllegalArgumentException("El tipo de alquiler es obligatorio.");
        }
        switch (tipoAlquiler.toUpperCase()) {
            case "COMUN":
                return new AlquilerComun(cliente, fechaEvento, cantidadDias, recargoComun);
            case "CORPORATIVO":
                return new AlquilerCorporativo(cliente, fechaEvento, cantidadDias, recargoCorporativo);
            case "MASIVO":
                return new AlquilerMasivo(cliente, fechaEvento, cantidadDias, recargoMasivo);
            default:
                throw new IllegalArgumentException("Tipo de alquiler desconocido: " + tipoAlquiler);
        }
    }

    private String obtenerTipoAlquiler(Alquiler alquiler) {
        if (alquiler instanceof AlquilerComun) {
            return "COMUN";
        }
        if (alquiler instanceof AlquilerCorporativo) {
            return "CORPORATIVO";
        }
        if (alquiler instanceof AlquilerMasivo) {
            return "MASIVO";
        }
        return null;
    }

    private Date calcularFechaFin(Date inicio, int cantidadDias) {
        if (inicio == null) {
            return null;
        }
        return new Date(inicio.getTime() + (long) cantidadDias * 24L * 60L * 60L * 1000L);
    }

    private boolean estaEnRango(Date fecha, Date desde, Date hasta) {
        if (fecha == null || desde == null || hasta == null) {
            return false;
        }
        return !fecha.before(desde) && !fecha.after(hasta);
    }

    /**
     * Reconstruye un Alquiler a partir de su DTO. Resuelve el cliente y los
     * equipos a traves de los controladores correspondientes. No modifica el
     * stock (la reserva se realiza en el flujo de solicitud).
     */
    public Alquiler toModel(AlquilerDTO dto) {
        Cliente cliente = ClienteController.getInstance().buscarPorDniCuit(dto.getDniCuitCliente());
        Alquiler alquiler = instanciarPorTipo(dto.getTipoAlquiler(), cliente,
                dto.getFechaEvento(), dto.getCantidadDias());
        if (dto.getFechaSolicitud() != null) {
            alquiler.setFechaSolicitud(dto.getFechaSolicitud());
        }
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            alquiler.setEstado(EstadoAlquiler.valueOf(dto.getEstado()));
        }
        alquiler.setSeniaAbonada(dto.getSeniaAbonada());

        if (dto.getDetalles() != null) {
            for (DetalleAlquilerDTO detalleDTO : dto.getDetalles()) {
                alquiler.agregarDetalle(toModelDetalle(detalleDTO));
            }
        }
        if (dto.getPagos() != null) {
            for (PagoDTO pagoDTO : dto.getPagos()) {
                alquiler.registrarPago(toModelPago(pagoDTO));
            }
        }
        return alquiler;
    }

    public AlquilerDTO toDTO(Alquiler modelo) {
        List<DetalleAlquilerDTO> detallesDTO = new ArrayList<>();
        for (DetalleAlquiler detalle : modelo.getDetalles()) {
            detallesDTO.add(toDTODetalle(detalle));
        }
        List<PagoDTO> pagosDTO = new ArrayList<>();
        for (Pago pago : modelo.getPagos()) {
            pagosDTO.add(toDTOPago(pago));
        }

        String dniCuitCliente = modelo.getCliente() != null ? modelo.getCliente().getDniCuit() : null;
        String estado = modelo.getEstado() != null ? modelo.getEstado().name() : null;

        return new AlquilerDTO(
                modelo.getId(), obtenerTipoAlquiler(modelo), dniCuitCliente,
                modelo.getFechaSolicitud(), modelo.getFechaEvento(), modelo.getCantidadDias(),
                estado, modelo.getSeniaAbonada(), modelo.getPorcentajeRecargoAplicado(),
                modelo.getImporteTotal(), modelo.getImportePendiente(), detallesDTO, pagosDTO);
    }

    public DetalleAlquiler toModelDetalle(DetalleAlquilerDTO dto) {
        Equipo equipo = EquipoController.getInstance().buscarPorCodigo(dto.getCodigoEquipo());
        return new DetalleAlquiler(dto.getCantidad(), dto.getValorDiarioAplicado(), equipo);
    }

    public DetalleAlquilerDTO toDTODetalle(DetalleAlquiler modelo) {
        String codigoEquipo = modelo.getEquipo() != null ? modelo.getEquipo().getCodigo() : null;
        return new DetalleAlquilerDTO(codigoEquipo, modelo.getCantidad(), modelo.getValorDiarioAplicado());
    }

    public Pago toModelPago(PagoDTO dto) {
        MedioPago medioPago = dto.getMedioPago() != null && !dto.getMedioPago().isBlank()
                ? MedioPago.valueOf(dto.getMedioPago())
                : null;
        Pago pago = new Pago(dto.getFecha(), dto.getImporte(), medioPago, dto.getUsuarioRegistro());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            pago.setEstado(org.examen.model.enums.EstadoPago.valueOf(dto.getEstado()));
        }
        return pago;
    }

    public PagoDTO toDTOPago(Pago modelo) {
        String medioPago = modelo.getMedioPago() != null ? modelo.getMedioPago().name() : null;
        String estado = modelo.getEstado() != null ? modelo.getEstado().name() : null;
        return new PagoDTO(
                modelo.getId(), modelo.getFecha(), modelo.getImporte(),
                medioPago, estado, modelo.getUsuarioRegistro());
    }
}
