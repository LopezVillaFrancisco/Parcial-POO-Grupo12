package org.examen.controller;

import org.examen.dto.AlquilerDTO;
import org.examen.dto.ClienteDTO;
import org.examen.dto.DetalleAlquilerDTO;
import org.examen.dto.EquipoDTO;
import org.examen.model.enums.TipoEquipo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueba 4 - Regla de negocio UC3.
 * Verifica que solicitarAlquiler() lance una excepcion cuando el equipo no tiene
 * stock suficiente para la cantidad solicitada.
 */
class AlquilerControllerStockTest {

    @Test
    @DisplayName("solicitarAlquiler falla si no hay stock suficiente")
    void solicitarAlquilerSinStockLanzaExcepcion() {
        String sufijo = String.valueOf(System.nanoTime());
        String dni = "CLI-" + sufijo;
        String codigoEquipo = "EQ-" + sufijo;

        ClienteController.getInstance().registrarCliente(
                new ClienteDTO(dni, "Cliente Test", "1111", "test@correo.com", "Calle 1", null, 0),
                "test");

        // Equipo con stock 1.
        EquipoController.getInstance().registrarEquipo(
                new EquipoDTO(codigoEquipo, "Parlante", "Sonido", TipoEquipo.SONIDO.name(),
                        null, 1000, 1, false),
                "test");

        // Se solicitan 5 unidades cuando solo hay 1 en stock.
        AlquilerDTO dto = new AlquilerDTO();
        dto.setDniCuitCliente(dni);
        dto.setTipoAlquiler("COMUN");
        dto.setFechaEvento(new Date());
        dto.setCantidadDias(2);
        dto.setDetalles(List.of(new DetalleAlquilerDTO(codigoEquipo, 5, 0)));

        assertThrows(IllegalStateException.class,
                () -> AlquilerController.getInstance().solicitarAlquiler(dto, "test"));
    }
}
