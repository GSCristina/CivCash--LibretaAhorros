package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

public class Gasto extends Movimiento{
    private String metodoPago;

    public Gasto() {
        super();
    }
    public Gasto(int idMovimiento, String concepto, double cantidad, LocalDate fecha, String categoria, int libreta, String metodoPago) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, libreta);
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
