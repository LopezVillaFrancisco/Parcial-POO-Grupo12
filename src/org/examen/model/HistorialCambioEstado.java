package org.examen.model;

import org.examen.model.enums.TipoEntidad;

import java.util.Date;

/**
 * Registro de auditoria de un cambio de estado sobre una entidad del sistema
 * (alquiler, pago, cliente o equipo). El campo referencia identifica a la
 * entidad afectada (por ejemplo, su id o codigo).
 */
public class HistorialCambioEstado {

    private Date fechaCambio;
    private String estadoAnterior;
    private String estadoNuevo;
    private TipoEntidad tipoEntidad;
    private String referencia;
    private String usuarioResponsable;

    public HistorialCambioEstado() {
    }

    public HistorialCambioEstado(Date fechaCambio, String estadoAnterior, String estadoNuevo,
                                 TipoEntidad tipoEntidad, String referencia, String usuarioResponsable) {
        this.fechaCambio = fechaCambio;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.tipoEntidad = tipoEntidad;
        this.referencia = referencia;
        this.usuarioResponsable = usuarioResponsable;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public TipoEntidad getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(TipoEntidad tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
