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

/**
 * Esta clase constituye la puerta de entrada segura a la aplicación. Se encarga de capturar
 * y sanear las credenciales de los usuarios, coordinar la validación de contraseñas mediante
 * el objeto de acceso a datos (DAO), gestionar el ciclo de apertura del contenedor global de
 * sesión (SesionController) y validar formalmente la estructura de los nuevos perfiles de usuario.
 */
public class LoginController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnEntrar;

    /**
     * Metodo con el cual gestionamos inicio de sesion de un usuario, este recupera las credenciales de la intezfaz y las valida mediante el DAO
     * si es correcto inicia sesion y carga el selector de libretas y si no es correcto mostrara una alerta de error.
     */
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

    /**
     * Metodo que sirve para cargar la interfaz del selector de libretas en la venta actual, utilizando el FXMLLoader para leer el archivo FXML correspondiente
     * configura la nueva escena en el escenario activo y se encarga de manejar posibles errores de entrada/salida
     */
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

    /**
     * Metodo que se encarga de gestionar el registro de un nuevo usuario, valida que los campos no esten vacios y que el que
     * el formato de correo sea correcto. Si todo es correcto dara de alta al usuario en el sistema a travez deñ DAO enviando
     * una notificacion al usuario y si falla mostrara una alerta de error
     */
    @FXML
    public void onRegisterClick() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, rellena el email y la contraseña para registrarte.");
            return;
        }
        if (!Util.validarEmail(email.trim())) {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Email no válido",
                    "El formato del correo electrónico introducido no es correcto.\nEjemplo válido: usuario@gmail.com");
            return;
        }
        Usuario nuevoUsuario = new Usuario(0, email.trim(), password);
        Usuario usuarioGuardado = UsuarioDAO.addUsuario(nuevoUsuario);
        if (usuarioGuardado != null) {
            Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "¡Usuario creado correctamente!\nAhora puedes darle a 'Entrar'.");
            txtPassword.clear();
        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error de Registro", "El email ingresado ya está registrado en el sistema.");
        }
    }
}
