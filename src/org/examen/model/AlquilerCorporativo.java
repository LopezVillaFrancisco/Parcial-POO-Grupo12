package org.examen.model;

import java.util.Date;

/**
 * Alquiler para evento corporativo: aplica el recargo recibido por constructor
 * (segun el diagrama), parametrizable de manera general desde el controlador
 * (por defecto 10%).
 */
public class AlquilerCorporativo extends Alquiler {

    private double recargo;

    public AlquilerCorporativo() {
        super();
    }

    public AlquilerCorporativo(Cliente cliente, Date fechaEvento, int cantidadDias, double recargo) {
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
