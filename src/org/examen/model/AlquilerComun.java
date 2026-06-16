package org.examen.model;

import java.util.Date;

/**
 * Alquiler comun: se calcula con el valor diario de los equipos.
 * El porcentaje de recargo se recibe por constructor (segun el diagrama),
 * permitiendo su parametrizacion general desde el controlador.
 */
public class AlquilerComun extends Alquiler {

    private double recargo;

    public AlquilerComun() {
        super();
    }

    public AlquilerComun(Cliente cliente, Date fechaEvento, int cantidadDias, double recargo) {
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
