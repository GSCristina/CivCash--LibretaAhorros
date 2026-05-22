package org.libretaahorros.libretaahorros.model;

import java.time.LocalDate;

/**
 * Entidad de modelo que representa un flujo de entrada de efectivo (Ingreso) en el sistema.
 * <p>
 * Esta clase extiende a la clase abstracta {@link Movimiento}, heredando sus propiedades
 * estructurales (concepto, cantidad, fecha, etc.) e incorporando el atributo especializado
 * de la procedencia u origen de los fondos. Implementa la redefinición polimórfica de la
 * firma de tipo para identificarse de forma unívoca dentro del sistema financiero.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public class Ingreso extends Movimiento {

    private String procedencia;

    /**
     * Constructor por defecto o sin parámetros de la entidad Ingreso.
     * <p>
     * Requerido obligatoriamente por los frameworks de Java y mecanismos de instanciación
     * dinámica. Invoca al constructor de la clase base {@code super()} para garantizar la
     * inicialización estructural de la jerarquía de objetos.
     */
    public Ingreso() {
        super();
    }

    /**
     * Constructor completo sobrecargado para inicializar un Ingreso con todas sus propiedades.
     * <p>
     * Setea los atributos comunes delegando en el constructor de la clase base {@link Movimiento}
     * mediante la instrucción {@code super} e inicializa el atributo local específico de la transacción.
     * * @param idMovimiento Identificador único de la transacción (clave primaria autogenerada).
     * @param concepto     Breve descripción o detalle del ingreso percibido.
     * @param cantidad     Monto numérico flotante del ingreso.
     * @param fecha        Fecha cronológica en la que se registra la operación.
     * @param categoria    Valor enumerado {@link Categoria} que tipifica la entrada de fondos.
     * @param idLibreta    Clave foránea de la libreta de ahorros a la que se vincula la operación.
     * @param responsable  Nombre de la persona u ordenador que ejecuta la transacción en el sistema.
     * @param procedencia  Origen o entidad de donde provienen los fondos (ej: Nómina, Regalo, Ventas).
     */
    public Ingreso(int idMovimiento, String concepto, double cantidad, LocalDate fecha, Categoria categoria, int idLibreta, String responsable, String procedencia) {
        super(idMovimiento, concepto, cantidad, fecha, categoria, idLibreta, responsable);
        this.procedencia = procedencia;
    }

    /**
     * Recupera la procedencia u origen del flujo de entrada de dinero.
     * * @return Cadena de texto con la entidad u origen del ingreso registrado.
     */
    public String getProcedencia() {
        return procedencia;
    }

    /**
     * Devuelve el literal identificador de la naturaleza polimórfica de la entidad.
     * * @return Cadena de texto estricta {@code "Ingreso"} que sobreescribe la firma del método abstracto.
     */
    @Override
    public String getTipo() {
        return "Ingreso";
    }
}
