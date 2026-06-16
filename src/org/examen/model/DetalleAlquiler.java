package org.examen.model;

/**
 * Linea de detalle de un alquiler: asocia un equipo con la cantidad solicitada
 * y el valor diario aplicado al momento del alquiler.
 */
public class DetalleAlquiler {

    private int cantidad;
    private double valorDiarioAplicado;
    private Equipo equipo;

    public DetalleAlquiler() {
    }

    public DetalleAlquiler(int cantidad, double valorDiarioAplicado, Equipo equipo) {
        this.cantidad = cantidad;
        this.valorDiarioAplicado = valorDiarioAplicado;
        this.equipo = equipo;
    }

    /**
     * Subtotal de la linea: cantidad * valor diario aplicado * cantidad de dias.
     */
    public double calcularSubtotal(int cantidadDias) {
        return this.cantidad * this.valorDiarioAplicado * cantidadDias;
    }

    public int obtenerCantidad() {
        return this.cantidad;
    }

    public Equipo obtenerEquipo() {
        return this.equipo;
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

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
