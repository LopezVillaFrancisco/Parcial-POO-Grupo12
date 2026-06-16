package org.examen.model;

import java.util.Date;

/**
 * Alquiler para evento masivo: aplica un recargo (por defecto 20%).
 * El porcentaje es parametrizable de manera general mediante un valor estatico.
 */
public class AlquilerMasivo extends Alquiler {

    private static double recargo = 20;

    public AlquilerMasivo() {
        super();
    }

    public AlquilerMasivo(Date fechaSolicitud, Date fechaEvento, int cantidadDias) {
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
