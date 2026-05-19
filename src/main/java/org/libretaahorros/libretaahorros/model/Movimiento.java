package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public abstract class Movimiento {
    protected int idMovimiento;
    protected String concepto;
    protected double cantidad;
    protected LocalDate fecha;
    protected Categoria categoria;
    protected int idLibreta;
    private String responsable;

    public Movimiento() {}

    public Movimiento(int idMovimiento, String concepto, double cantidad, LocalDate fecha, Categoria categoria, int idLibreta, String responsable) {
        this.idMovimiento = idMovimiento;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
        this.idLibreta = idLibreta;
        this.responsable = responsable;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getIdLibreta() {
        return idLibreta;
    }

    public void setIdLibreta(int idLibreta) {
        this.idLibreta = idLibreta;
    }
    public abstract String getTipo();
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
}
