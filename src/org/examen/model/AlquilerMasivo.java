package org.examen.model;

import java.util.Date;

/**
 * Alquiler para evento masivo: aplica el recargo recibido por constructor
 * (segun el diagrama), parametrizable de manera general desde el controlador
 * (por defecto 20%).
 */
public class AlquilerMasivo extends Alquiler {

    private double recargo;

    public AlquilerMasivo() {
        super();
    }

    public AlquilerMasivo(Cliente cliente, Date fechaEvento, int cantidadDias, double recargo) {
        super(cliente, fechaEvento, cantidadDias);
        this.recargo = recargo;
    }

    @Override
    public double obtenerPorcentajeRecargo() {
        return recargo;
    }

    public double getRecargo() {
        return recargo;
    }

    public void setRecargo(double recargo) {
        this.recargo = recargo;
    }
}
