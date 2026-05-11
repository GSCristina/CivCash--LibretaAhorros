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

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnRegistrar;

    @FXML
    public void onLoginClick() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        Usuario user = UsuarioDAO.validarLogin(email, pass);

        if (user != null) {
            System.out.println("Login exitoso. Cargando MainView...");
            cargarVentanaPrincipal();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de acceso");
            alert.setHeaderText(null);
            alert.setContentText("Email o contraseña incorrectos.");
            alert.showAndWait();
        }
    }
    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/main_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnEntrar.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Libreta de Ahorros - Pantalla Principal");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterClick() {
        System.out.println("Navegando al registro...");
    }
}
