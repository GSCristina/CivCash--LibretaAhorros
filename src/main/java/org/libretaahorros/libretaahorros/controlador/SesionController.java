package org.libretaahorros.libretaahorros.controlador;

import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.Usuario;

/**
 * Controlador de gestion y estado de la sesion de la aplicacion, se encarga de almacenar en memoria los datos del usuario
 * logueado, las libretas activas y el rol de seguridad asignado en cada una de ellas.
 */
public class SesionController {
    private static Usuario usuarioLogueado;
    private static Libreta libretaSeleccionada;
    private static String rolActual;

    /**
     * Metodo para guardar al usuario tras un login exitoso
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    /**
     * Almacena en el contexto global de la sesión la libreta que el usuario ha seleccionado.
     * @param libreta El objeto Libreta que se va a gestionar activamente en la aplicación.
     */
    public static void setLibreta(Libreta libreta) {
        libretaSeleccionada = libreta;
    }
    /**
     * Recupera la libreta seleccionada activamente en la sesión actual.
     * @return El objeto Libreta cargado en memoria, o null si no hay ninguna selección activa.
     */
    public static Libreta getLibreta() {
        return libretaSeleccionada;
    }

    /**
     *Metodo para obtener los datos del usuario desde cualquier pantalla
     */
    public static Usuario getUsuario() {
        return usuarioLogueado;
    }
    public static void setRolActual(String rol) {
        rolActual = rol;
    }
    /**
     * Recupera el rol o nivel de permisos (Propietario o Invitado) que el usuario autenticado posee sobre la libreta
     * en la que está operando.
     * @return el rol actual del usuario.
     */
    public static String getRolActual() {
        return rolActual;
    }

    /**
     * Para cuando el usuario quiera salir de la app
     */
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}
