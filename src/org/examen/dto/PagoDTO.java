package org.examen.dto;

import java.util.Date;

/**
 * DTO plano para transportar datos de Pago entre las vistas y los
 * controladores. No contiene logica de negocio.
 */
public class PagoDTO {

    private int id;
    private Date fecha;
    private double importe;
    private String medioPago;
    private String estado;
    private String usuarioRegistro;

    public PagoDTO() {
    }

    public PagoDTO(int id, Date fecha, double importe, String medioPago,
                   String estado, String usuarioRegistro) {
        this.id = id;
        this.fecha = fecha;
        this.importe = importe;
        this.medioPago = medioPago;
        this.estado = estado;
        this.usuarioRegistro = usuarioRegistro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
}
