package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        System.out.println("Email: " + txtEmail.getText());
        System.out.println("Intentando iniciar sesión...");
    }

    @FXML
    public void onRegisterClick() {
        System.out.println("Navegando al registro...");
    }
}
