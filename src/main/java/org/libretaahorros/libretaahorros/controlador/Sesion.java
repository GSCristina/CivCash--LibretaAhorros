package org.libretaahorros.libretaahorros.controlador;

import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.Usuario;

public class Sesion {
    private static Usuario usuarioLogueado;
    private static Libreta libretaSeleccionada;

    /**
     * Metodo para guardar al usuario tras un login exitoso
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioLogueado = usuario;
    }
    public static void setLibreta(Libreta libreta) {
        libretaSeleccionada = libreta;
    }

    public static Libreta getLibreta() {
        return libretaSeleccionada;
    }

    /**
     *Metodo para obtener los datos del usuario desde cualquier pantalla
     */
    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    /**
     * Para cuando el usuario quiera salir de la app
     */
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}
