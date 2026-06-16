package org.examen.dto;

import java.util.Date;

/**
 * DTO plano para transportar datos de un registro de auditoria de cambio de
 * estado entre las vistas y los controladores. No contiene logica de negocio.
 */
public class HistorialCambioEstadoDTO {

    private Date fechaCambio;
    private String estadoAnterior;
    private String estadoNuevo;
    private String tipoEntidad;
    private String referencia;
    private String usuarioResponsable;

    public HistorialCambioEstadoDTO() {
    }

    public HistorialCambioEstadoDTO(Date fechaCambio, String estadoAnterior, String estadoNuevo,
                                    String tipoEntidad, String referencia, String usuarioResponsable) {
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

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
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
