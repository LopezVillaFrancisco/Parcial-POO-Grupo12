package org.examen.model;

import org.examen.model.enums.EstadoPago;
import org.examen.model.enums.MedioPago;

import java.util.Date;

/**
 * Pago realizado sobre un alquiler. El identificador se genera de forma
 * automatica e incremental al instanciarse.
 */
public class Pago {

    private static int contadorId = 0;

    private int id;
    private Date fecha;
    private double importe;
    private MedioPago medioPago;
    private EstadoPago estado;
    private String usuarioRegistro;

    public Pago() {
        this.id = ++contadorId;
        this.estado = EstadoPago.REGISTRADO;
    }

    public Pago(Date fecha, double importe, MedioPago medioPago, String usuarioRegistro) {
        this();
        this.fecha = fecha;
        this.importe = importe;
        this.medioPago = medioPago;
        this.usuarioRegistro = usuarioRegistro;
    }

    public void confirmar() {
        this.estado = EstadoPago.CONFIRMADO;
    }

    public void anular() {
        this.estado = EstadoPago.ANULADO;
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

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
}
