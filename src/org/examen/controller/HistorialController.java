package org.examen.controller;

import org.examen.dto.HistorialCambioEstadoDTO;
import org.examen.model.HistorialCambioEstado;
import org.examen.model.enums.TipoEntidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador Singleton encargado de la auditoria de cambios de estado del
 * sistema. Mantiene en memoria la lista de registros de historial.
 */
public class HistorialController {

    private static HistorialController instance;

    private final List<HistorialCambioEstado> listaHistoriales;

    private HistorialController() {
        this.listaHistoriales = new ArrayList<>();
    }

    public static HistorialController getInstance() {
        if (instance == null) {
            instance = new HistorialController();
        }
        return instance;
    }

    /**
     * Registra un cambio de estado en la auditoria. La fecha del cambio se toma
     * al momento de registrar.
     */
    public HistorialCambioEstadoDTO registrar(String estadoAnterior, String estadoNuevo,
                                              TipoEntidad tipoEntidad, String referencia,
                                              String usuarioResponsable) {
        HistorialCambioEstado historial = new HistorialCambioEstado(
                new Date(), estadoAnterior, estadoNuevo, tipoEntidad, referencia, usuarioResponsable);
        listaHistoriales.add(historial);
        return toDTO(historial);
    }

    /**
     * Devuelve los registros de auditoria correspondientes a una entidad y su
     * referencia (por ejemplo, todos los cambios del cliente con un DNI/CUIT).
     */
    public List<HistorialCambioEstadoDTO> obtenerPorEntidad(TipoEntidad tipoEntidad, String referencia) {
        List<HistorialCambioEstadoDTO> resultado = new ArrayList<>();
        for (HistorialCambioEstado historial : listaHistoriales) {
            if (historial.getTipoEntidad() == tipoEntidad
                    && historial.getReferencia() != null
                    && historial.getReferencia().equals(referencia)) {
                resultado.add(toDTO(historial));
            }
        }
        return resultado;
    }

    public List<HistorialCambioEstadoDTO> obtenerTodos() {
        List<HistorialCambioEstadoDTO> resultado = new ArrayList<>();
        for (HistorialCambioEstado historial : listaHistoriales) {
            resultado.add(toDTO(historial));
        }
        return resultado;
    }

    public HistorialCambioEstado toModel(HistorialCambioEstadoDTO dto) {
        TipoEntidad tipoEntidad = dto.getTipoEntidad() != null
                ? TipoEntidad.valueOf(dto.getTipoEntidad())
                : null;
        return new HistorialCambioEstado(
                dto.getFechaCambio(), dto.getEstadoAnterior(), dto.getEstadoNuevo(),
                tipoEntidad, dto.getReferencia(), dto.getUsuarioResponsable());
    }

    public HistorialCambioEstadoDTO toDTO(HistorialCambioEstado modelo) {
        String tipoEntidad = modelo.getTipoEntidad() != null
                ? modelo.getTipoEntidad().name()
                : null;
        return new HistorialCambioEstadoDTO(
                modelo.getFechaCambio(), modelo.getEstadoAnterior(), modelo.getEstadoNuevo(),
                tipoEntidad, modelo.getReferencia(), modelo.getUsuarioResponsable());
    }
}
