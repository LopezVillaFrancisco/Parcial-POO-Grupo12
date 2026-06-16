package org.examen.model;

import org.examen.model.enums.EstadoAlquiler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Alquiler de equipamiento. Clase abstracta: el porcentaje de recargo se
 * resuelve polimorficamente segun el tipo concreto de alquiler.
 *
 * Mantiene como composicion las listas de detalles y de pagos.
 * El identificador se genera de forma automatica e incremental al instanciarse.
 */
public abstract class Alquiler {

    private static int contadorId = 0;

    private int id;
    private Cliente cliente;
    private Date fechaSolicitud;
    private Date fechaEvento;
    private int cantidadDias;
    private EstadoAlquiler estado;
    private double seniaAbonada;
    private double porcentajeRecargoAplicado;
    private double importeTotal;
    private double importePendiente;
    private List<DetalleAlquiler> detalles;
    private List<Pago> pagos;

    protected Alquiler() {
        this.id = ++contadorId;
        this.fechaSolicitud = new Date();
        this.estado = EstadoAlquiler.INGRESADO;
        this.detalles = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    protected Alquiler(Cliente cliente, Date fechaEvento, int cantidadDias) {
        this();
        this.cliente = cliente;
        this.fechaEvento = fechaEvento;
        this.cantidadDias = cantidadDias;
    }

    /**
     * Porcentaje de recargo aplicable segun el tipo de alquiler.
     * Resuelto polimorficamente por cada subclase.
     */
    public abstract double obtenerPorcentajeRecargo();

    public void agregarDetalle(DetalleAlquiler detalle) {
        this.detalles.add(detalle);
    }

    public void registrarPago(Pago pago) {
        this.pagos.add(pago);
    }

    public void registrarSenia(double importe) {
        this.seniaAbonada += importe;
    }

    public void confirmar() {
        this.estado = EstadoAlquiler.CONFIRMADO;
    }

    public void cancelar() {
        this.estado = EstadoAlquiler.CANCELADO;
    }

    public void pasarAEnPreparacion() {
        this.estado = EstadoAlquiler.EN_PREPARACION;
    }

    public void entregar() {
        this.estado = EstadoAlquiler.ENTREGADO;
    }

    public void finalizar() {
        this.estado = EstadoAlquiler.FINALIZADO;
    }

    /**
     * Suma de los subtotales de todos los detalles, considerando la cantidad
     * de dias del alquiler.
     */
    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleAlquiler detalle : detalles) {
            subtotal += detalle.calcularSubtotal(cantidadDias);
        }
        return subtotal;
    }

    /**
     * Importe total: subtotal con el recargo del tipo de alquiler aplicado y el
     * descuento particular del cliente. Almacena el resultado y el recargo
     * efectivamente aplicado.
     */
    public double calcularImporteTotal(double descuentoCliente) {
        this.porcentajeRecargoAplicado = obtenerPorcentajeRecargo();
        double subtotal = calcularSubtotal();
        double conRecargo = subtotal * (1 + porcentajeRecargoAplicado / 100.0);
        this.importeTotal = conRecargo * (1 - descuentoCliente / 100.0);
        return this.importeTotal;
    }

    /**
     * Importe pendiente: importe total menos la senia abonada.
     */
    public double calcularImportePendiente(double descuentoCliente) {
        double total = calcularImporteTotal(descuentoCliente);
        this.importePendiente = total - this.seniaAbonada;
        return this.importePendiente;
    }

    /**
     * Horas de anticipacion entre la fecha de cancelacion y la fecha del evento.
     */
    public int calcularHorasAnticipacion(Date fechaCancelacion) {
        if (fechaEvento == null || fechaCancelacion == null) {
            return 0;
        }
        long diferenciaMillis = fechaEvento.getTime() - fechaCancelacion.getTime();
        return (int) (diferenciaMillis / (1000 * 60 * 60));
    }

    /**
     * Fecha de fin del periodo del alquiler (fecha del evento mas la cantidad
     * de dias contratados).
     */
    public Date getFechaFin() {
        if (fechaEvento == null) {
            return null;
        }
        long fin = fechaEvento.getTime() + (long) cantidadDias * 24L * 60L * 60L * 1000L;
        return new Date(fin);
    }

    /**
     * Indica si el alquiler mantiene equipos comprometidos, es decir, si no fue
     * cancelado ni finalizado.
     */
    public boolean ocupaStock() {
        return estado == EstadoAlquiler.INGRESADO
                || estado == EstadoAlquiler.CONFIRMADO
                || estado == EstadoAlquiler.EN_PREPARACION
                || estado == EstadoAlquiler.ENTREGADO;
    }

    /**
     * Indica si el periodo del alquiler se solapa con el rango [desde, hasta].
     */
    public boolean seSolapaConPeriodo(Date desde, Date hasta) {
        Date fin = getFechaFin();
        if (fechaEvento == null || fin == null || desde == null || hasta == null) {
            return false;
        }
        return !fechaEvento.after(hasta) && !fin.before(desde);
    }

    /**
     * Cantidad total del equipo indicado reservada por este alquiler.
     */
    public int cantidadReservadaDeEquipo(String codigoEquipo) {
        int total = 0;
        for (DetalleAlquiler detalle : detalles) {
            if (detalle.getEquipo() != null
                    && codigoEquipo.equals(detalle.getEquipo().getCodigo())) {
                total += detalle.getCantidad();
            }
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlquiler estado) {
        this.estado = estado;
    }

    public double getSeniaAbonada() {
        return seniaAbonada;
    }

    public void setSeniaAbonada(double seniaAbonada) {
        this.seniaAbonada = seniaAbonada;
    }

    public double getPorcentajeRecargoAplicado() {
        return porcentajeRecargoAplicado;
    }

    public void setPorcentajeRecargoAplicado(double porcentajeRecargoAplicado) {
        this.porcentajeRecargoAplicado = porcentajeRecargoAplicado;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public double getImportePendiente() {
        return importePendiente;
    }

    public void setImportePendiente(double importePendiente) {
        this.importePendiente = importePendiente;
    }

    public List<DetalleAlquiler> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleAlquiler> detalles) {
        this.detalles = detalles;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
