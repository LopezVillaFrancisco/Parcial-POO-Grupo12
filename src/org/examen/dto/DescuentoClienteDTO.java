package org.examen.dto;

import java.util.Date;

/**
 * DTO plano para transportar datos de un descuento de cliente entre las vistas
 * y los controladores. No contiene logica de negocio.
 */
public class DescuentoClienteDTO {

    private double porcentaje;
    private Date fechaDesde;
    private Date fechaHasta;

    public DescuentoClienteDTO() {
    }

    public DescuentoClienteDTO(double porcentaje, Date fechaDesde, Date fechaHasta) {
        this.porcentaje = porcentaje;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
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
