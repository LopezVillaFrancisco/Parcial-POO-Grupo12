package org.examen.model;

import java.util.Date;

/**
 * Alquiler para evento corporativo: aplica un recargo (por defecto 10%).
 * El porcentaje es parametrizable de manera general mediante un valor estatico.
 */
public class AlquilerCorporativo extends Alquiler {

    private static double recargo = 10;

    public AlquilerCorporativo() {
        super();
    }

    public AlquilerCorporativo(Date fechaSolicitud, Date fechaEvento, int cantidadDias) {
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
