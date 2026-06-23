package org.jefrycruz.models;

import java.util.Date;

public class Compras {

    private int codigoCompra;
    private Date fechaCompra;
    private Double total;
    private String detalle;
    private int codigoProveedor;

    public Compras() {
    }

    public Compras(int codigoCompra, Date fechaCompra, Double total, String detalle, int codigoProveedor) {
        this.codigoCompra = codigoCompra;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.detalle = detalle;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(int codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
    
    
    
}
