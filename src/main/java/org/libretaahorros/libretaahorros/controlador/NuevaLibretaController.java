package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.libretaahorros.libretaahorros.DAO.LibretaDAO;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.utils.Util;

/**
 * Esta clase gobierna el cuadro de diálogo modal auxiliar encargado de capturar los datos
 * de entrada para dar de alta una cuenta. Implementa validaciones en la capa de presentación
 * y delega en el Singleton del DAO la persistencia de la nueva entidad, gestionando de forma
 * simultánea la lógica de compartición opcional con otros usuarios mediante invitaciones por correo.
 */
public class NuevaLibretaController {
    @FXML private TextField txtNombre;
    @FXML private Button btnGuardar;
    @FXML private TextField txtEmailAmigo;
    @FXML private Button btnCancelar;

    /**
     * Metodo que gestiona la creacion de una nueva libreta, la cual permite ser compartida, extrae los datos de la interfaz y aplica
     * restricciones de validacion sobre el nombre de esta, delegando al DAO la persistencia de la entidad y la asociacion con el
     * usuario actual mediante el ID de la sesion.
     * Si el registro es exitoso cierra la ventana utilizando el boton de guardar, si no muestra una alerta de error.
     */
    @FXML
    public void clickGuardar() {
        String nombre = txtNombre.getText();
        String emailAmigo = txtEmailAmigo.getText().trim();

        if (nombre == null || nombre.trim().isEmpty()) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "El nombre de la libreta no puede estar vacío.");
            return;
        }

        Libreta nuevaLibreta = new Libreta();
        nuevaLibreta.setNombre(nombre.trim());
        nuevaLibreta.setSaldoActual(0.0);

        int idUser = SesionController.getUsuario().getIdUsuario();
        boolean guardadoExito = LibretaDAO.getInstance().addLibretaCompartida(nuevaLibreta, idUser, emailAmigo);

        if (guardadoExito) {
            Util.cerrarVentanaDesde(btnGuardar);
        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la libreta en la base de datos.");
        }
    }
    /**
     * Gestiona el evento de cancelación de la pantalla al pulsar el botón correspondiente.
     * Delega la destrucción de la vista en otro metodo
     */
    @FXML
    public void clickCancelar() {
        cerrarVentana();
    }
    /**
     * Recupera el escenario ("Stage") actual de JavaFX a través del botón cancelar
     * y procede a cerrar la ventana de forma segura.
     */
    private void cerrarVentana() {
        Util.cerrarVentanaDesde(btnCancelar);
    }

}
