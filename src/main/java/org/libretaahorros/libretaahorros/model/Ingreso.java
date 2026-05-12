package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public class Ingreso extends Movimiento{
    private String procedencia;

    public Ingreso() { super(); }

    public Ingreso(int idMovimiento, String concepto, double cantidad, LocalDate fecha, String categoria, int libreta, String procedencia) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, libreta);
        this.procedencia = procedencia;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }
}
