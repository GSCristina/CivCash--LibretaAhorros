package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

/**
 * Entidad de modelo que representa un flujo de salida de efectivo (Gasto) en el sistema.
 * <p>
 * Esta clase extiende a la clase abstracta {@link Movimiento}, heredando sus propiedades
 * estructurales (concepto, cantidad, fecha, etc.) e incorporando el atributo especializado
 * del mecanismo de pago empleado. Implementa la redefinición polimórfica de la firma de tipo
 * para identificarse de forma unívoca dentro del sistema financiero.
 */
public class Gasto extends Movimiento {

    private String metodoPago;

    /**
     * Constructor por defecto o sin parámetros de la entidad Gasto.
     * <p>
     * Requerido obligatoriamente por los frameworks de Java y mecanismos de instanciación
     * dinámica. Invoca al constructor de la clase base {@code super()} para garantizar la
     * inicialización estructural de la jerarquía de objetos.
     */
    public Gasto() {
        super();
    }

    /**
     * Constructor completo sobrecargado para inicializar un Gasto con todas sus propiedades.
     * <p>
     * Setea los atributos comunes delegando en el constructor de la clase base {@link Movimiento}
     * mediante la instrucción {@code super} e inicializa el atributo local específico de la transacción.
     * * @param idMovimiento Identificador único de la transacción (clave primaria autogenerada).
     * @param concepto     Breve descripción o detalle del gasto efectuado.
     * @param cantidad     Monto numérico flotante del gasto.
     * @param fecha        Fecha cronológica en la que se registra la operación.
     * @param categoria    Valor enumerado {@link Categoria} que tipifica el coste.
     * @param idLibreta    Clave foránea de la libreta de ahorros a la que se vincula la operación.
     * @param responsable  Nombre de la persona u ordenador que ejecuta la transacción en el sistema.
     * @param metodoPago   Mecanismo físico o digital utilizado para abonar el importe (ej: Tarjeta, Efectivo).
     */
    public Gasto(int idMovimiento, String concepto, double cantidad, LocalDate fecha, Categoria categoria, int idLibreta, String responsable, String metodoPago) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, idLibreta, responsable);
        this.metodoPago = metodoPago;
    }

    /**
     * Recupera el metodo de pago o canal financiero utilizado para liquidar el gasto.
     * * @return Cadena de texto con el mecanismo de pago registrado.
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Devuelve el literal identificador de la naturaleza polimórfica de la entidad.
     * * @return Cadena de texto estricta {@code "Gasto"} que sobreescribe la firma del metodo abstracto.
     */
    @Override
    public String getTipo() {
        return "Gasto";
    }
}
