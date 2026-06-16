package org.examen.dto;

/**
 * DTO plano para transportar datos de una linea de detalle de alquiler entre
 * las vistas y los controladores. No contiene logica de negocio.
 */
public class DetalleAlquilerDTO {

    private String codigoEquipo;
    private int cantidad;
    private double valorDiarioAplicado;

    public DetalleAlquilerDTO() {
    }

    public DetalleAlquilerDTO(String codigoEquipo, int cantidad, double valorDiarioAplicado) {
        this.codigoEquipo = codigoEquipo;
        this.cantidad = cantidad;
        this.valorDiarioAplicado = valorDiarioAplicado;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorDiarioAplicado() {
        return valorDiarioAplicado;
    }

    public void setValorDiarioAplicado(double valorDiarioAplicado) {
        this.valorDiarioAplicado = valorDiarioAplicado;
    }
}
