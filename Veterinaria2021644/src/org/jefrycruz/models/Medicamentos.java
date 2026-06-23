
package org.jefrycruz.models;

import java.util.Date;


public class Medicamentos {
    private int codigoMedicamento;
    private String nombre;
    private String descripcion;
    private int stock;
    private Double precioUnitario;
    private Date fechaVencimiento;
    private int codigoProveedor;

    public Medicamentos() {
    }

    public Medicamentos(int codigoMedicamento, String nombre, String descripcion, int stock, Double precioUnitario, Date fechaVencimiento, int codigoProveedor) {
        this.codigoMedicamento = codigoMedicamento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precioUnitario = precioUnitario;
        this.fechaVencimiento = fechaVencimiento;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
    
    
    
}
