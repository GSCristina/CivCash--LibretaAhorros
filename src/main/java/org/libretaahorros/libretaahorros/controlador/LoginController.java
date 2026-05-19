package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.DAO.UsuarioDAO;
import org.libretaahorros.libretaahorros.model.Usuario;
import org.libretaahorros.libretaahorros.utils.Util;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnEntrar;

    @FXML
    public void onLoginClick() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        Usuario usuario = UsuarioDAO.validarLogin(email, password);

        if (usuario != null) {
            System.out.println("Login exitoso. Cargando Libretas...");
            SesionController.iniciarSesion(usuario);
            cargarSelectorLibretas();

        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error de acceso", "Email o contraseña incorrectos.");
        }
    }
    private void cargarSelectorLibretas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/selectorLibreta.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnEntrar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mis Libretas");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el selector: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterClick() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        if (email == null || email.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, rellena el email y la contraseña para registrarte.");
            return;
        }
        Usuario nuevoUsuario = new Usuario(0, email, pass);
        Usuario usuarioGuardado = UsuarioDAO.addUsuario(nuevoUsuario);
        if (usuarioGuardado != null) {
            Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "¡Usuario creado correctamente!\nAhora puedes darle a 'Entrar'.");
            txtPassword.clear();
        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error de Registro", "El email ingresado ya está registrado en el sistema.");
        }
    }
}
