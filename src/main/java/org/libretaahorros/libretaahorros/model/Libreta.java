package org.libretaahorros.libretaahorros.model;

import java.util.ArrayList;
import java.util.List;

public class Libreta {
    private int idLibreta;
    private String nombre;
    private double saldoActual;
    private List<Movimiento> movimientos;
    private List<Usuario> usuarios;


    public Libreta() {
        this.movimientos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }
    public Libreta(int idLibreta, String nombre, double saldoActual) {
        this.idLibreta = idLibreta;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.movimientos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int getIdLibreta() {
        return idLibreta;
    }

    public void setIdLibreta(int idLibreta) {
        this.idLibreta = idLibreta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
    public void addMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }

    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }
    @Override
    public String toString() {
        return this.nombre;
    }
}
