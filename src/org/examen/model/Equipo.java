package org.examen.model;

import org.examen.model.enums.EstadoEquipo;
import org.examen.model.enums.TipoEquipo;

/**
 * Equipo disponible para alquilar. Se identifica por su codigo.
 */
public class Equipo {

    private String codigo;
    private String nombre;
    private String descripcion;
    private TipoEquipo tipoEquipo;
    private EstadoEquipo estado;
    private double valorDiario;
    private int stockDisponible;
    private boolean requiereInstalacion;

    public Equipo() {
        this.estado = EstadoEquipo.DISPONIBLE;
    }

    public Equipo(String codigo, String nombre, String descripcion, TipoEquipo tipoEquipo,
                  double valorDiario, int stockDisponible, boolean requiereInstalacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoEquipo = tipoEquipo;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.valorDiario = valorDiario;
        this.stockDisponible = stockDisponible;
        this.requiereInstalacion = requiereInstalacion;
    }

    /**
     * El equipo esta disponible si su estado es DISPONIBLE y posee stock
     * suficiente para la cantidad solicitada.
     */
    public boolean estaDisponible(int cantidad) {
        return this.estado == EstadoEquipo.DISPONIBLE && this.stockDisponible >= cantidad;
    }

    public void reservarStock(int cantidad) {
        this.stockDisponible -= cantidad;
    }

    public void liberarStock(int cantidad) {
        this.stockDisponible += cantidad;
    }

    public void cambiarEstado(EstadoEquipo estadoNuevo) {
        this.estado = estadoNuevo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoEquipo getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(TipoEquipo tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public void setValorDiario(double valorDiario) {
        this.valorDiario = valorDiario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public boolean isRequiereInstalacion() {
        return requiereInstalacion;
    }

    public void setRequiereInstalacion(boolean requiereInstalacion) {
        this.requiereInstalacion = requiereInstalacion;
    }
}
