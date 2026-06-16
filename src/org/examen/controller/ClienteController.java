package org.examen.controller;

import org.examen.dto.ClienteDTO;
import org.examen.dto.DescuentoClienteDTO;
import org.examen.model.Cliente;
import org.examen.model.DescuentoCliente;
import org.examen.model.enums.EstadoCliente;
import org.examen.model.enums.TipoEntidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador Singleton de clientes. Mantiene en memoria la lista de clientes
 * registrados y coordina el caso de uso "Registrar Cliente" (UC1).
 */
public class ClienteController {

    private static ClienteController instance;

    private final List<Cliente> listaClientes;

    private ClienteController() {
        this.listaClientes = new ArrayList<>();
    }

    public static ClienteController getInstance() {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }

    /**
     * UC1 - Registrar Cliente.
     * Valida que el DNI/CUIT no este repetido, crea el cliente, lo activa, lo
     * agrega a la lista y registra el alta en la auditoria.
     */
    public ClienteDTO registrarCliente(ClienteDTO dto, String usuario) {
        if (dto == null || dto.getDniCuit() == null || dto.getDniCuit().isBlank()) {
            throw new IllegalArgumentException("El DNI/CUIT es obligatorio.");
        }
        if (buscarPorDniCuit(dto.getDniCuit()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el DNI/CUIT " + dto.getDniCuit());
        }

        Cliente cliente = toModel(dto);
        cliente.activar();
        listaClientes.add(cliente);

        HistorialController.getInstance().registrar(
                "-", cliente.getEstado().name(), TipoEntidad.CLIENTE, cliente.getDniCuit(), usuario);

        return toDTO(cliente);
    }

    /**
     * Agrega un descuento particular a un cliente existente.
     */
    public ClienteDTO agregarDescuento(String dniCuit, DescuentoClienteDTO descuentoDTO) {
        Cliente cliente = buscarPorDniCuit(dniCuit);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el DNI/CUIT " + dniCuit);
        }
        cliente.agregarDescuento(toModelDescuento(descuentoDTO));
        return toDTO(cliente);
    }

    /**
     * Busca el modelo Cliente por su DNI/CUIT. Pensado para la comunicacion
     * entre controladores (devuelve el modelo, no el DTO).
     */
    public Cliente buscarPorDniCuit(String dniCuit) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getDniCuit() != null && cliente.getDniCuit().equals(dniCuit)) {
                return cliente;
            }
        }
        return null;
    }

    public ClienteDTO obtenerCliente(String dniCuit) {
        Cliente cliente = buscarPorDniCuit(dniCuit);
        return cliente != null ? toDTO(cliente) : null;
    }

    public List<ClienteDTO> obtenerTodos() {
        List<ClienteDTO> resultado = new ArrayList<>();
        for (Cliente cliente : listaClientes) {
            resultado.add(toDTO(cliente));
        }
        return resultado;
    }

    public Cliente toModel(ClienteDTO dto) {
        Cliente cliente = new Cliente(
                dto.getDniCuit(), dto.getNombreRazonSocial(), dto.getTelefono(),
                dto.getEmail(), dto.getDireccion());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            cliente.setEstado(EstadoCliente.valueOf(dto.getEstado()));
        }
        cliente.setCreditoAFavor(dto.getCreditoAFavor());
        return cliente;
    }

    public ClienteDTO toDTO(Cliente modelo) {
        String estado = modelo.getEstado() != null ? modelo.getEstado().name() : null;
        return new ClienteDTO(
                modelo.getDniCuit(), modelo.getNombreRazonSocial(), modelo.getTelefono(),
                modelo.getEmail(), modelo.getDireccion(), estado, modelo.getCreditoAFavor());
    }

    public DescuentoCliente toModelDescuento(DescuentoClienteDTO dto) {
        return new DescuentoCliente(dto.getPorcentaje(), dto.getFechaDesde(), dto.getFechaHasta());
    }

    public DescuentoClienteDTO toDTODescuento(DescuentoCliente modelo) {
        return new DescuentoClienteDTO(
                modelo.getPorcentaje(), modelo.getFechaDesde(), modelo.getFechaHasta());
    }
}
