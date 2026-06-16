package org.examen.dto;

/**
 * DTO plano para transportar datos de Equipo entre las vistas y los
 * controladores. No contiene logica de negocio.
 */
public class EquipoDTO {

    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoEquipo;
    private String estado;
    private double valorDiario;
    private int stockDisponible;
    private boolean requiereInstalacion;

    public EquipoDTO() {
    }

    public EquipoDTO(String codigo, String nombre, String descripcion, String tipoEquipo,
                     String estado, double valorDiario, int stockDisponible, boolean requiereInstalacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoEquipo = tipoEquipo;
        this.estado = estado;
        this.valorDiario = valorDiario;
        this.stockDisponible = stockDisponible;
        this.requiereInstalacion = requiereInstalacion;
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

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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
