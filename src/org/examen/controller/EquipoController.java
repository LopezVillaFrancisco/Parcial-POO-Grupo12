package org.examen.controller;

import org.examen.dto.EquipoDTO;
import org.examen.model.Equipo;
import org.examen.model.enums.EstadoEquipo;
import org.examen.model.enums.TipoEntidad;
import org.examen.model.enums.TipoEquipo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador Singleton de equipos. Mantiene en memoria la lista de equipos y
 * coordina los casos de uso "Registrar Equipo" (UC2) y "Consultar equipos
 * disponibles" (UC5).
 */
public class EquipoController {

    private static EquipoController instance;

    private final List<Equipo> listaEquipos;

    private EquipoController() {
        this.listaEquipos = new ArrayList<>();
    }

    public static EquipoController getInstance() {
        if (instance == null) {
            instance = new EquipoController();
        }
        return instance;
    }

    /**
     * UC2 - Registrar Equipo.
     * Valida que el codigo no este repetido, crea el equipo (estado DISPONIBLE
     * por defecto), lo agrega a la lista y registra el alta en la auditoria.
     */
    public EquipoDTO registrarEquipo(EquipoDTO dto, String usuario) {
        if (dto == null || dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El codigo del equipo es obligatorio.");
        }
        if (buscarPorCodigo(dto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con el codigo " + dto.getCodigo());
        }

        Equipo equipo = toModel(dto);
        listaEquipos.add(equipo);

        HistorialController.getInstance().registrar(
                "-", equipo.getEstado().name(), TipoEntidad.EQUIPO, equipo.getCodigo(), usuario);

        return toDTO(equipo);
    }

    /**
     * UC5 - Consultar equipos disponibles.
     * Recorre los equipos y devuelve aquellos cuyo tipo coincide con el tipo de
     * evento solicitado y que se encuentran disponibles.
     */
    public List<EquipoDTO> consultarEquiposDisponibles(Date fechaEvento, int cantidadDias, String tipoEvento) {
        TipoEquipo tipo = TipoEquipo.valueOf(tipoEvento);
        List<EquipoDTO> disponibles = new ArrayList<>();
        for (Equipo equipo : listaEquipos) {
            if (equipo.getTipoEquipo() == tipo && equipo.estaDisponible(1)) {
                disponibles.add(toDTO(equipo));
            }
        }
        return disponibles;
    }

    /**
     * Busca el modelo Equipo por su codigo. Pensado para la comunicacion entre
     * controladores (devuelve el modelo, no el DTO).
     */
    public Equipo buscarPorCodigo(String codigo) {
        for (Equipo equipo : listaEquipos) {
            if (equipo.getCodigo() != null && equipo.getCodigo().equals(codigo)) {
                return equipo;
            }
        }
        return null;
    }

    public EquipoDTO obtenerEquipo(String codigo) {
        Equipo equipo = buscarPorCodigo(codigo);
        return equipo != null ? toDTO(equipo) : null;
    }

    public List<EquipoDTO> obtenerTodos() {
        List<EquipoDTO> resultado = new ArrayList<>();
        for (Equipo equipo : listaEquipos) {
            resultado.add(toDTO(equipo));
        }
        return resultado;
    }

    public Equipo toModel(EquipoDTO dto) {
        TipoEquipo tipoEquipo = dto.getTipoEquipo() != null && !dto.getTipoEquipo().isBlank()
                ? TipoEquipo.valueOf(dto.getTipoEquipo())
                : null;
        Equipo equipo = new Equipo(
                dto.getCodigo(), dto.getNombre(), dto.getDescripcion(), tipoEquipo,
                dto.getValorDiario(), dto.getStockDisponible(), dto.isRequiereInstalacion());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            equipo.setEstado(EstadoEquipo.valueOf(dto.getEstado()));
        }
        return equipo;
    }

    public EquipoDTO toDTO(Equipo modelo) {
        String tipoEquipo = modelo.getTipoEquipo() != null ? modelo.getTipoEquipo().name() : null;
        String estado = modelo.getEstado() != null ? modelo.getEstado().name() : null;
        return new EquipoDTO(
                modelo.getCodigo(), modelo.getNombre(), modelo.getDescripcion(), tipoEquipo,
                estado, modelo.getValorDiario(), modelo.getStockDisponible(),
                modelo.isRequiereInstalacion());
    }
}
