package org.libretaahorros.libretaahorros.model;

public class UsuarioLibreta {
    private int idUsuario;
    private Libreta libreta;
    private String rol;

    public UsuarioLibreta() {

    }

    public UsuarioLibreta(int idUsuario, Libreta libreta, String rol) {
        this.idUsuario = idUsuario;
        this.libreta = libreta;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Libreta getLibreta() {
        return libreta;
    }

    public void setLibreta(Libreta libreta) {
        this.libreta = libreta;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
