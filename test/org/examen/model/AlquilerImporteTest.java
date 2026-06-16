package org.examen.model;

import org.examen.model.enums.TipoEquipo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba 2 - Calculo de importe.
 * Verifica que calcularImportePendiente() considere valor diario, cantidad de
 * dias, recargo del tipo de alquiler, descuento del cliente y senia abonada.
 */
class AlquilerImporteTest {

    @Test
    @DisplayName("calcularImportePendiente aplica recargo, descuento y senia")
    void calcularImportePendienteAplicaTodosLosFactores() {
        Equipo equipo = new Equipo("EQ-TEST", "Parlante", "Sonido",
                TipoEquipo.SONIDO, 1000, 10, false);

        // Alquiler comun con recargo del 10% y 3 dias de duracion.
        Alquiler alquiler = new AlquilerComun(null, new Date(), 3, 10);
        // 2 unidades x 1000 (valor diario) x 3 dias = 6000 de subtotal.
        alquiler.agregarDetalle(new DetalleAlquiler(2, equipo.getValorDiario(), equipo));
        alquiler.registrarSenia(1000);

        // 6000 * 1,10 = 6600 ; 6600 * (1 - 0,05) = 6270 ; 6270 - 1000 = 5270.
        double pendiente = alquiler.calcularImportePendiente(5.0);

        assertEquals(6270.0, alquiler.getImporteTotal(), 0.0001);
        assertEquals(5270.0, pendiente, 0.0001);
    }
}
