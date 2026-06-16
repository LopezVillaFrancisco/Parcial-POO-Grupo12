package org.examen.model;

import java.util.Date;

/**
 * Alquiler comun: se calcula con el valor diario de los equipos, sin recargo.
 * El porcentaje es parametrizable de manera general mediante un valor estatico.
 */
public class AlquilerComun extends Alquiler {

    private static double recargo = 0;

    public AlquilerComun() {
        super();
    }

    public AlquilerComun(Date fechaSolicitud, Date fechaEvento, int cantidadDias) {
        super(fechaSolicitud, fechaEvento, cantidadDias);
    }

    @Override
    public double obtenerPorcentajeRecargo() {
        return recargo;
    }

    public static double getRecargo() {
        return recargo;
    }

    public static void setRecargo(double nuevoRecargo) {
        recargo = nuevoRecargo;
    }
}
