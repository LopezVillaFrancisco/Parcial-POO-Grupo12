package org.examen.model;

import java.util.Date;

/**
 * Descuento particular asociado a un cliente, con un periodo de vigencia.
 */
public class DescuentoCliente {

    private double porcentaje;
    private Date fechaDesde;
    private Date fechaHasta;

    public DescuentoCliente() {
    }

    public DescuentoCliente(double porcentaje, Date fechaDesde, Date fechaHasta) {
        this.porcentaje = porcentaje;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    /**
     * Indica si el descuento se encuentra vigente para la fecha indicada
     * (limites inclusive).
     */
    public boolean estaVigente(Date fecha) {
        if (fecha == null || fechaDesde == null || fechaHasta == null) {
            return false;
        }
        return !fecha.before(fechaDesde) && !fecha.after(fechaHasta);
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
