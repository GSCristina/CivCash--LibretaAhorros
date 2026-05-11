package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public class Ingreso extends Movimiento{
    private String procedencia;

    public Ingreso() { super(); }

    public Ingreso(int idMovimiento, String concepto, double cantidad, LocalDate fecha, String categoria, int libreta, String fuente) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, libreta);
        this.procedencia = fuente;
    }

    public String getFuente() {
        return procedencia;
    }

    public void setFuente(String fuente) {
        this.procedencia = fuente;
    }
}
