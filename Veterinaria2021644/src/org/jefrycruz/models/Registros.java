package org.jefrycruz.models;

public class Registros {

    private int codigoRegistro;
    private String usuario;
    private String contrasena;

    public Registros() {
    }

    public Registros(int codigoRegistro, String usuario, String contrasena) {
        this.codigoRegistro = codigoRegistro;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public int getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(int codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
