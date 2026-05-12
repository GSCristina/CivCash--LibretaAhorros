package org.libretaahorros.libretaahorros.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int idUsuario;
    private  String email;
    private String password;
    private List<Libreta> libretas;

    public Usuario() {
        this.libretas = new ArrayList<>();
    }

    public Usuario(int idUsuario, String email, String password) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.password = password;
        this.libretas = new ArrayList<>();
    }

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
        this.libretas = new ArrayList<>();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Libreta> getLibretas() {
        return libretas;
    }

    public void setLibretas(List<Libreta> libretas) {
        this.libretas = libretas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", email='" + email + '\'' +
                '}';
    }
}
