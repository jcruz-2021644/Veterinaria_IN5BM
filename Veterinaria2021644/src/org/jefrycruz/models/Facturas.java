package org.jefrycruz.models;

import java.util.Date;


public class Facturas {
    private int codigoFactura;
    private Date fechaEmision;
    private Double total;
    private Pago metodoPago ;
    private int codigoCliente;
    private int codigoEmpleado;
    
    public enum Pago{
    Efectivo, Tarjeta, Transferencia
    }

    public Facturas() {
    }

    public Facturas(int codigoFactura, Date fechaEmision, Double total, Pago metodoPago, int codigoCliente, int codigoEmpleado) {
        this.codigoFactura = codigoFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.metodoPago = metodoPago;
        this.codigoCliente = codigoCliente;
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Pago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(Pago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
    
    
}
