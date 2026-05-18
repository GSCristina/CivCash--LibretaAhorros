package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.DAO.LibretaDAO;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.utils.Util;

public class NuevaLibretaController {
    @FXML private TextField txtNombre;
    @FXML private Button btnGuardar;
    @FXML private TextField txtEmailAmigo;

    @FXML
    public void onGuardarClick() {
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
        boolean guardadoExito = LibretaDAO.addLibreta(nuevaLibreta, idUser, emailAmigo);

        if (guardadoExito) {
            cerrarVentana();
        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la libreta en la base de datos.");
        }
    }

    @FXML
    public void onCancelarClick() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

}
