package org.jefrycruz.models;

import java.time.LocalTime;
import java.util.Date;

public class Citas {
    private int codigoCita;
    private Date fechaCita; 
    private LocalTime horaCita;
    private String motivo;
    private int codigoMascota;
    private int codigoVeterinario; 
    private Estado estado;
    
    public enum Estado {
        Pendiente,Completa,Cancelada
    }

    public Citas() {
    }

    public Citas(int codigoCita, Date fechaCita, LocalTime horaCita, String motivo, int codigoMascota, int codigoVeterinario, Estado estado) {
        this.codigoCita = codigoCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.motivo = motivo;
        this.codigoMascota = codigoMascota;
        this.codigoVeterinario = codigoVeterinario;
        this.estado = estado;
    }

    public int getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(LocalTime horaCita) {
        this.horaCita = horaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getCodigoMascota() {
        return codigoMascota;
    }

    public void setCodigoMascota(int codigoMascota) {
        this.codigoMascota = codigoMascota;
    }

    public int getCodigoVeterinario() {
        return codigoVeterinario;
    }

    public void setCodigoVeterinario(int codigoVeterinario) {
        this.codigoVeterinario = codigoVeterinario;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    
}
