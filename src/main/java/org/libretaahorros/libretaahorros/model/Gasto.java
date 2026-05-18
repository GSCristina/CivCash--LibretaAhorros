package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public class Gasto extends Movimiento{
    private String metodoPago;

    public Gasto() {
        super();
    }
    public Gasto(int idMovimiento, String concepto, double cantidad, LocalDate fecha, String categoria, int idLibreta, String responsable, String metodoPago) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, idLibreta, responsable);
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    @Override
    public String getTipo() {
        return "Gasto";
    }
}
