package org.examen.controller;

import org.examen.dto.ClienteDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueba 3 - Regla de negocio UC1.
 * Verifica que registrarCliente() rechace un DNI/CUIT ya existente.
 * Usa un DNI/CUIT unico para ser independiente del estado del Singleton.
 */
class ClienteControllerTest {

    @Test
    @DisplayName("registrarCliente rechaza un DNI/CUIT duplicado")
    void registrarClienteRechazaDniCuitDuplicado() {
        ClienteController controller = ClienteController.getInstance();
        String dni = "DUP-" + System.nanoTime();

        ClienteDTO original = new ClienteDTO(dni, "Cliente Test", "1111",
                "test@correo.com", "Calle 123", null, 0);
        controller.registrarCliente(original, "test");

        ClienteDTO duplicado = new ClienteDTO(dni, "Otro Cliente", "2222",
                "otro@correo.com", "Calle 456", null, 0);

        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarCliente(duplicado, "test"));
    }
}
