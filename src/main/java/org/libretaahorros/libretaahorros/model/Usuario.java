package org.libretaahorros.libretaahorros.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de modelo que representa a un usuario dentro del sistema.
 * <p>
 * Esta clase encapsula las credenciales de acceso de las cuentas (correo electrónico
 * y contraseña) y mantiene la referencia relacional hacia las libretas de ahorro
 * a las que tiene acceso el perfil, dando soporte al ecosistema multiusuario de la aplicación.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public class Usuario {

    private int idUsuario;
    private String email;
    private String password;
    private List<Libreta> libretas;

    /**
     * Constructor por defecto o sin parámetros de la entidad Usuario.
     * <p>
     * Requerido por los frameworks y mecanismos de instanciación reflexiva de Java.
     * Inicializa de forma segura la colección de libretas como una instancia vacía
     * de {@link ArrayList} para prevenir excepciones de puntero nulo.
     */
    public Usuario() {
        this.libretas = new ArrayList<>();
    }

    /**
     * Constructor completo sobrecargado para inicializar un usuario con sus credenciales fundamentales.
     * <p>
     * Asigna las variables de estado primarias de autenticación del perfil e inicializa
     * internamente la lista vacía de asociaciones a libretas de ahorros.
     * * @param idUsuario Identificador único del usuario (clave primaria autogenerada en MySQL).
     * @param email     Dirección de correo electrónico única que actúa como login del usuario.
     * @param password  Cadena de texto con la contraseña de acceso asociada a la cuenta.
     */
    public Usuario(int idUsuario, String email, String password) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.password = password;
        this.libretas = new ArrayList<>();
    }

    /**
     * Recupera la clave primaria identificativa del usuario en el sistema.
     * * @return Valor entero del identificador único de la base de datos.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Recupera la dirección de correo electrónico del usuario.
     * * @return Cadena de texto con el email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Recupera la contraseña de acceso asociada a la cuenta del usuario.
     * * @return Cadena de texto con la credencial del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Devuelve una representación en formato de texto legible de los datos esenciales del objeto.
     * <p>
     * Sobreescribe el comportamiento nativo de la clase Object. Excluye intencionadamente
     * el atributo de la contraseña por estrictos criterios de seguridad y protección de
     * datos confidenciales en los logs del sistema.
     * * @return Cadena de texto formateada con el ID y el correo electrónico del usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", email='" + email + '\'' +
                '}';
    }
}
