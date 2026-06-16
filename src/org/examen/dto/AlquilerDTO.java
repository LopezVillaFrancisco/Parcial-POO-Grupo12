package org.examen.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO plano para transportar datos de Alquiler entre las vistas y los
 * controladores. El campo tipoAlquiler indica el tipo concreto
 * (COMUN, CORPORATIVO o MASIVO). No contiene logica de negocio.
 */
public class AlquilerDTO {

    private int id;
    private String tipoAlquiler;
    private String dniCuitCliente;
    private Date fechaSolicitud;
    private Date fechaEvento;
    private int cantidadDias;
    private String estado;
    private double seniaAbonada;
    private double porcentajeRecargoAplicado;
    private double importeTotal;
    private double importePendiente;
    private List<DetalleAlquilerDTO> detalles;
    private List<PagoDTO> pagos;

    public AlquilerDTO() {
        this.detalles = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    public AlquilerDTO(int id, String tipoAlquiler, String dniCuitCliente, Date fechaSolicitud,
                       Date fechaEvento, int cantidadDias, String estado, double seniaAbonada,
                       double porcentajeRecargoAplicado, double importeTotal, double importePendiente,
                       List<DetalleAlquilerDTO> detalles, List<PagoDTO> pagos) {
        this.id = id;
        this.tipoAlquiler = tipoAlquiler;
        this.dniCuitCliente = dniCuitCliente;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaEvento = fechaEvento;
        this.cantidadDias = cantidadDias;
        this.estado = estado;
        this.seniaAbonada = seniaAbonada;
        this.porcentajeRecargoAplicado = porcentajeRecargoAplicado;
        this.importeTotal = importeTotal;
        this.importePendiente = importePendiente;
        this.detalles = detalles;
        this.pagos = pagos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoAlquiler() {
        return tipoAlquiler;
    }

    public void setTipoAlquiler(String tipoAlquiler) {
        this.tipoAlquiler = tipoAlquiler;
    }

    public String getDniCuitCliente() {
        return dniCuitCliente;
    }

    public void setDniCuitCliente(String dniCuitCliente) {
        this.dniCuitCliente = dniCuitCliente;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getSeniaAbonada() {
        return seniaAbonada;
    }

    public void setSeniaAbonada(double seniaAbonada) {
        this.seniaAbonada = seniaAbonada;
    }

    public double getPorcentajeRecargoAplicado() {
        return porcentajeRecargoAplicado;
    }

    public void setPorcentajeRecargoAplicado(double porcentajeRecargoAplicado) {
        this.porcentajeRecargoAplicado = porcentajeRecargoAplicado;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public double getImportePendiente() {
        return importePendiente;
    }

    public void setImportePendiente(double importePendiente) {
        this.importePendiente = importePendiente;
    }

    public List<DetalleAlquilerDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleAlquilerDTO> detalles) {
        this.detalles = detalles;
    }

    public List<PagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoDTO> pagos) {
        this.pagos = pagos;
    }
}
