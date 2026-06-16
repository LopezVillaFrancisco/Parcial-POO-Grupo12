package org.examen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba 1 - Polimorfismo.
 * Verifica que cada tipo concreto de alquiler resuelve su porcentaje de recargo
 * de forma polimorfica a traves de la referencia a la clase abstracta Alquiler.
 */
class PolimorfismoRecargoTest {

    @Test
    @DisplayName("Cada tipo de alquiler resuelve su recargo polimorficamente")
    void cadaTipoResuelveSuRecargo() {
        Date fecha = new Date();

        Alquiler comun = new AlquilerComun(null, fecha, 1, 0);
        Alquiler corporativo = new AlquilerCorporativo(null, fecha, 1, 10);
        Alquiler masivo = new AlquilerMasivo(null, fecha, 1, 20);

        assertEquals(0.0, comun.obtenerPorcentajeRecargo(), 0.0001);
        assertEquals(10.0, corporativo.obtenerPorcentajeRecargo(), 0.0001);
        assertEquals(20.0, masivo.obtenerPorcentajeRecargo(), 0.0001);
    }
}
