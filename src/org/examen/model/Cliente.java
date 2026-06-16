package org.examen.model;

import org.examen.model.enums.EstadoCliente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cliente que solicita alquileres. Se identifica por su DNI/CUIT.
 * Mantiene como composicion la lista de sus descuentos particulares.
 */
public class Cliente {

    private String dniCuit;
    private String nombreRazonSocial;
    private String telefono;
    private String email;
    private String direccion;
    private EstadoCliente estado;
    private double creditoAFavor;
    private List<DescuentoCliente> descuentos;

    public Cliente() {
        this.descuentos = new ArrayList<>();
        this.estado = EstadoCliente.ACTIVO;
        this.creditoAFavor = 0;
    }

    public Cliente(String dniCuit, String nombreRazonSocial, String telefono,
                   String email, String direccion) {
        this();
        this.dniCuit = dniCuit;
        this.nombreRazonSocial = nombreRazonSocial;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public void activar() {
        this.estado = EstadoCliente.ACTIVO;
    }

    public void inactivar() {
        this.estado = EstadoCliente.INACTIVO;
    }

    public void agregarCredito(double importe) {
        this.creditoAFavor += importe;
    }

    public void agregarDescuento(DescuentoCliente descuento) {
        this.descuentos.add(descuento);
    }

    /**
     * Devuelve el porcentaje del primer descuento vigente para la fecha dada,
     * o 0 si el cliente no posee descuentos vigentes.
     */
    public double obtenerDescuentoVigente(Date fecha) {
        for (DescuentoCliente descuento : descuentos) {
            if (descuento.estaVigente(fecha)) {
                return descuento.getPorcentaje();
            }
        }
        return 0;
    }

    public boolean estaActivo() {
        return this.estado == EstadoCliente.ACTIVO;
    }

    public String getDniCuit() {
        return dniCuit;
    }

    public void setDniCuit(String dniCuit) {
        this.dniCuit = dniCuit;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public double getCreditoAFavor() {
        return creditoAFavor;
    }

    public void setCreditoAFavor(double creditoAFavor) {
        this.creditoAFavor = creditoAFavor;
    }

    public List<DescuentoCliente> getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(List<DescuentoCliente> descuentos) {
        this.descuentos = descuentos;
    }
}
