package org.libretaahorros.libretaahorros.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de modelo que representa una libreta de ahorros en el sistema.
 * <p>
 * Esta clase funciona como el contenedor central del negocio financiero, encapsulando
 * los atributos identificativos primarios de la cuenta (nombre y saldo contable actual).
 * Mantiene las referencias estructurales para las colecciones asociadas y se integra
 * de forma directa con los componentes de la interfaz gráfica.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public class Libreta {

    private int idLibreta;
    private String nombre;
    private double saldoActual;
    private List<Movimiento> movimientos;
    private List<Usuario> usuarios;

    /**
     * Constructor por defecto o sin parámetros de la entidad Libreta.
     * <p>
     * Requerido obligatoriamente por los frameworks de Java y mecanismos de instanciación reflexiva.
     * Inicializa de forma segura las colecciones de movimientos y usuarios como instancias vacías
     * de {@link ArrayList} para mitigar excepciones de puntero nulo (NullPointerException).
     */
    public Libreta() {
        this.movimientos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    /**
     * Constructor sobrecargado para inicializar una libreta con sus propiedades contables fundamentales.
     * <p>
     * Asigna las variables de estado primarias del registro de la cuenta e inicializa internamente
     * las listas vacías de asociaciones para garantizar la integridad de las colecciones.
     * * @param idLibreta   Identificador único de la cuenta (clave primaria autogenerada en MySQL).
     * @param nombre      Denominación comercial o título asignado a la libreta por el usuario.
     * @param saldoActual Monto monetario líquido disponible actualmente en la cuenta.
     */
    public Libreta(int idLibreta, String nombre, double saldoActual) {
        this.idLibreta = idLibreta;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.movimientos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    /**
     * Recupera la clave primaria identificativa de la libreta de ahorros.
     * * @return Valor entero del identificador único en la base de datos.
     */
    public int getIdLibreta() {
        return idLibreta;
    }

    /**
     * Establece el identificador único de la libreta.
     * <p>
     * Utilizado principalmente por la capa DAO tras capturar las claves autogeneradas por el servidor.
     * * @param idLibreta Valor numérico del nuevo identificador primario.
     */
    public void setIdLibreta(int idLibreta) {
        this.idLibreta = idLibreta;
    }

    /**
     * Recupera el nombre asignado a la libreta.
     * * @return Cadena de texto con el título identificativo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica la denominación o nombre de la libreta en memoria.
     * * @param nombre Cadena de texto con el nuevo título de la cuenta.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Recupera el balance contable o saldo líquido actual de la libreta.
     * * @return Valor numérico double con el saldo de la cuenta.
     */
    public double getSaldoActual() {
        return saldoActual;
    }

    /**
     * Modifica el saldo contable líquido actual de la libreta.
     * * @param saldoActual Nuevo monto numérico double que actualiza el estado contable.
     */
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
    /**
     * Devuelve una representación en formato de texto legible del objeto.
     * <p>
     * Sobreescribe el comportamiento nativo de la clase Object. Es utilizado de forma directa
     * por los componentes visuales de JavaFX (como el ComboBox o ListView) para renderizar de
     * forma limpia el nombre de la libreta en la interfaz gráfica.
     * * @return Cadena de texto que contiene el nombre de la libreta.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}
