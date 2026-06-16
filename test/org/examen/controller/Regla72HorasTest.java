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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba 5 - Regla de negocio de cancelacion (72 horas).
 * Verifica que al cancelar un alquiler confirmado con mas de 72 horas de
 * anticipacion la senia quede como credito a favor del cliente, y que con menos
 * de 72 horas la senia no se reintegre.
 */
class Regla72HorasTest {

    private static final long UN_DIA_MS = 24L * 60L * 60L * 1000L;
    private static final long UNA_HORA_MS = 60L * 60L * 1000L;
    private static final double SENIA = 500.0;

    @Test
    @DisplayName("Cancelar con mas de 72 hs acredita la senia como credito a favor")
    void cancelarConMasDe72HorasAcreditaSenia() {
        String escenario = registrarEscenario("MAS72");
        String dni = escenario.split("\\|")[0];
        int idAlquiler = crearAlquilerConfirmado(escenario);

        // Evento dentro de 10 dias, cancelacion hoy => mas de 72 hs de anticipacion.
        AlquilerController.getInstance().cancelarAlquiler(idAlquiler, new Date(), "test");

        double credito = ClienteController.getInstance().obtenerCliente(dni).getCreditoAFavor();
        assertEquals(SENIA, credito, 0.0001);
    }

    @Test
    @DisplayName("Cancelar con menos de 72 hs no reintegra la senia")
    void cancelarConMenosDe72HorasNoReintegra() {
        String escenario = registrarEscenario("MENOS72");
        String dni = escenario.split("\\|")[0];
        int idAlquiler = crearAlquilerConfirmado(escenario);

        AlquilerDTO alquiler = AlquilerController.getInstance().obtenerAlquiler(idAlquiler);
        // Cancelacion una hora antes del evento => menos de 72 hs de anticipacion.
        Date fechaCancelacion = new Date(alquiler.getFechaEvento().getTime() - UNA_HORA_MS);
        AlquilerController.getInstance().cancelarAlquiler(idAlquiler, fechaCancelacion, "test");

        double credito = ClienteController.getInstance().obtenerCliente(dni).getCreditoAFavor();
        assertEquals(0.0, credito, 0.0001);
    }

    /** Registra un cliente y un equipo unicos; devuelve "dni|codigoEquipo". */
    private String registrarEscenario(String etiqueta) {
        String sufijo = etiqueta + "-" + System.nanoTime();
        String dni = "CLI-" + sufijo;
        String codigoEquipo = "EQ-" + sufijo;

        ClienteController.getInstance().registrarCliente(
                new ClienteDTO(dni, "Cliente Test", "1111", "test@correo.com", "Calle 1", null, 0),
                "test");
        EquipoController.getInstance().registrarEquipo(
                new EquipoDTO(codigoEquipo, "Parlante", "Sonido", TipoEquipo.SONIDO.name(),
                        null, 1000, 10, false),
                "test");

        return dni + "|" + codigoEquipo;
    }

    /** Solicita y confirma un alquiler con evento dentro de 10 dias. */
    private int crearAlquilerConfirmado(String escenario) {
        String dni = escenario.split("\\|")[0];
        String codigoEquipo = escenario.split("\\|")[1];

        AlquilerDTO dto = new AlquilerDTO();
        dto.setDniCuitCliente(dni);
        dto.setTipoAlquiler("COMUN");
        dto.setFechaEvento(new Date(System.currentTimeMillis() + 10 * UN_DIA_MS));
        dto.setCantidadDias(1);
        dto.setDetalles(List.of(new DetalleAlquilerDTO(codigoEquipo, 1, 0)));

        AlquilerDTO creado = AlquilerController.getInstance().solicitarAlquiler(dto, "test");
        AlquilerController.getInstance().confirmarAlquiler(creado.getId(), SENIA, "EFECTIVO", "test");
        return creado.getId();
    }
}
