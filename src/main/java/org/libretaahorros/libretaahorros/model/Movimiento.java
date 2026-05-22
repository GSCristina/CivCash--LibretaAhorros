package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

/**
 * Clase abstracta que define la estructura base y el comportamiento común de un movimiento financiero.
 * <p>
 * Sirve como superclase conceptual dentro de la jerarquía de herencia del modelo de negocio,
 * unificando las propiedades comunes de cualquier transacción (como cantidad, fecha o categoría).
 * Al estar declarada como {@code abstract}, impide su instanciación directa y delega en sus
 * especializaciones polimórficas ({@link Ingreso} y {@link Gasto}) la definición de su naturaleza.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public abstract class Movimiento {

    protected int idMovimiento;
    protected String concepto;
    protected double cantidad;
    protected LocalDate fecha;
    protected Categoria categoria;
    protected int idLibreta;
    private String responsable;

    /**
     * Constructor por defecto o sin parámetros de la entidad abstracta Movimiento.
     * <p>
     * Requerido por los frameworks y mecanismos de instanciación reflexiva de Java para
     * inicializar las estructuras base de la jerarquía de objetos.
     */
    public Movimiento() {}

    /**
     * Constructor completo sobrecargado para inicializar la estructura base de un movimiento.
     * * @param idMovimiento Identificador único de la transacción (clave primaria autogenerada en MySQL).
     * @param concepto     Breve descripción o detalle aclaratorio de la transacción.
     * @param cantidad     Monto numérico de la operación.
     * @param fecha        Fecha cronológica en la que se ejecuta y registra la transacción.
     * @param categoria    Valor enumerado {@link Categoria} que clasifica la naturaleza del flujo.
     * @param idLibreta    Clave foránea de la libreta de ahorros a la que pertenece operativamente.
     * @param responsable  Nombre del usuario o elemento que registra la operación en el sistema.
     */
    public Movimiento(int idMovimiento, String concepto, double cantidad, LocalDate fecha, Categoria categoria, int idLibreta, String responsable) {
        this.idMovimiento = idMovimiento;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
        this.idLibreta = idLibreta;
        this.responsable = responsable;
    }

    /**
     * Recupera el identificador único del movimiento.
     * * @return Valor numérico de la clave primaria.
     */
    public int getIdMovimiento() {
        return idMovimiento;
    }

    /**
     * Establece o modifica el identificador único del movimiento.
     * * @param idMovimiento Valor numérico del nuevo identificador primario.
     */
    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    /**
     * Recupera la descripción o concepto del movimiento.
     * * @return Cadena de texto con el detalle del movimiento.
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Modifica el concepto o descripción de la transacción en memoria.
     * * @param concepto Cadena de texto con la nueva descripción.
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * Recupera el monto económico de la transacción.
     * * @return Valor numérico double con el importe.
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * Modifica el monto económico de la transacción en memoria.
     * * @param cantidad Nuevo importe numérico de la transacción.
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Recupera la fecha de ejecución de la operación financiera.
     * * @return Objeto {@link LocalDate} con el registro temporal.
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Modifica el registro temporal de la transacción en memoria.
     * * @param fecha Objeto {@link LocalDate} con la nueva fecha de asignación.
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Recupera el tipo clasificatorio o etiqueta de negocio asignada a la operación.
     * * @return El valor del enumerado {@link Categoria}.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Asigna o modifica la categoría clasificatoria del movimiento.
     * * @param categoria El nuevo valor del enumerado {@link Categoria}.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Recupera el identificador de la libreta de ahorros a la que está vinculado el movimiento.
     * * @return Valor entero con el identificador de la clave foránea.
     */
    public int getIdLibreta() {
        return idLibreta;
    }

    /**
     * Metodo abstracto diseñado para forzar la resolución polimórfica de la naturaleza del movimiento.
     * <p>
     * Las clases hijas que extiendan de esta clase deberán sobreescribir obligatoriamente este método
     * para retornar la cadena literal correspondiente a su identidad de flujo.
     * * @return Cadena de texto identificativa de la especialización (ej: "Ingreso" o "Gasto").
     */
    public abstract String getTipo();

    /**
     * Recupera el nombre de la entidad o responsable que introdujo la transacción en la libreta.
     * * @return Cadena de texto con la identidad del responsable.
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * Modifica la propiedad del responsable de la ejecución del movimiento en memoria.
     * * @param responsable Cadena de texto con la nueva identidad del responsable.
     */
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}
