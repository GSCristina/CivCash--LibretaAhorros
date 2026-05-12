package org.libretaahorros.libretaahorros.model;

public class UsuarioLibreta {
    private int idUsuario;
    private int idLibreta;

    public UsuarioLibreta() {

    }

    public UsuarioLibreta(int idUsuario, int idLibreta) {
        this.idUsuario = idUsuario;
        this.idLibreta = idLibreta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLibreta() {
        return idLibreta;
    }

    public void setIdLibreta(int idLibreta) {
        this.idLibreta = idLibreta;
    }
}
