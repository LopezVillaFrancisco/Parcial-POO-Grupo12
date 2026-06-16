package org.examen.dto;

/**
 * DTO plano para transportar datos de Cliente entre las vistas y los
 * controladores. No contiene logica de negocio.
 */
public class ClienteDTO {

    private String dniCuit;
    private String nombreRazonSocial;
    private String telefono;
    private String email;
    private String direccion;
    private String estado;
    private double creditoAFavor;

    public ClienteDTO() {
    }

    public ClienteDTO(String dniCuit, String nombreRazonSocial, String telefono, String email,
                      String direccion, String estado, double creditoAFavor) {
        this.dniCuit = dniCuit;
        this.nombreRazonSocial = nombreRazonSocial;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.creditoAFavor = creditoAFavor;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCreditoAFavor() {
        return creditoAFavor;
    }

    public void setCreditoAFavor(double creditoAFavor) {
        this.creditoAFavor = creditoAFavor;
    }
}
