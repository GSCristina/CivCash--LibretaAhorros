package org.libretaahorros.libretaahorros.model;

/**
 * Entidad asociativa de modelo que representa la relación relacional y operativa entre un Usuario y una Libreta.
 * <p>
 * Rompe la relación de muchos a muchos (N:M) existente en la base de datos relacional.
 * Además de vincular las claves de ambas entidades, añade valor de negocio al almacenar
 * el atributo específico del nivel de acceso o privilegios (Rol) que posee el usuario
 * sobre una cuenta concreta (ej: Propietario o Invitado).
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public class UsuarioLibreta {

    private int idUsuario;
    private Libreta libreta;
    private String rol;

    /**
     * Constructor por defecto o sin parámetros de la entidad UsuarioLibreta.
     * <p>
     * Requerido obligatoriamente por los frameworks de Java y mecanismos de instanciación
     * dinámica para inicializar la estructura en memoria de forma genérica.
     */
    public UsuarioLibreta() {

    }

    /**
     * Constructor completo sobrecargado para inicializar la asociación con todos sus parámetros.
     * * @param idUsuario Identificador único del usuario participante (clave foránea).
     * @param libreta   Instancia de tipo {@link Libreta} vinculada en la asociación (clave foránea).
     * @param rol       Cadena de texto que dictamina los permisos del usuario en la libreta (Propietario/Invitado).
     */
    public UsuarioLibreta(int idUsuario, Libreta libreta, String rol) {
        this.idUsuario = idUsuario;
        this.libreta = libreta;
        this.rol = rol;
    }

    /**
     * Recupera el identificador único del usuario vinculado a la relación.
     * * @return Valor entero del identificador del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Setea o modifica el identificador del usuario en la asociación.
     * * @param idUsuario Valor numérico del nuevo identificador de usuario.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Recupera la instancia de la libreta de ahorros vinculada a la relación.
     * * @return El objeto de tipo {@link Libreta}.
     */
    public Libreta getLibreta() {
        return libreta;
    }

    /**
     * Vincula o modifica la instancia de la libreta en la asociación.
     * * @param libreta El nuevo objeto de tipo {@link Libreta} a asociar.
     */
    public void setLibreta(Libreta libreta) {
        this.libreta = libreta;
    }

    /**
     * Recupera el rol o nivel de privilegios asignado al usuario para esta libreta.
     * * @return Cadena de texto con la denominación del rol.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Modifica el rol o nivel de privilegios del usuario en esta libreta específica.
     * * @param rol Cadena de texto con el nuevo rol (ej: Propietario, Invitado).
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
