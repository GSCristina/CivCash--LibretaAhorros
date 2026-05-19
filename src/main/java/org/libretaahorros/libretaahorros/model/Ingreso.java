package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public class Ingreso extends Movimiento{
    private String procedencia;

    public Ingreso() { super(); }

    public Ingreso(int idMovimiento, String concepto, double cantidad, LocalDate fecha, Categoria categoria, int idLibreta, String responsable, String procedencia) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, idLibreta, responsable);
        this.procedencia = procedencia;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }
    @Override
    public String getTipo() {
        return "Ingreso";
    }
}
