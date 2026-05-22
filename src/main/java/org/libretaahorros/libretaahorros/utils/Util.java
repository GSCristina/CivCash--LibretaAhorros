package org.libretaahorros.libretaahorros.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.stage.Window;
import org.libretaahorros.libretaahorros.model.Categoria;

public class Util {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    /**
     * Valida si un String tiene un formato de email correcto.
     * @param email El texto a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /**
     * Muestra una alerta en pantalla de forma centralizada.
     * @param tipo Tipo de alerta (INFORMATION, WARNING, ERROR, CONFIRMATION...)
     * @param titulo Título de la ventana de la alerta
     * @param mensaje Mensaje de texto principal
     */
    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    /**
     * Cambia la escena de la ventana actual de forma genérica.
     * @param nodoReferencia Cualquier componente de la pantalla actual (ej: un botón o la tabla)
     * @param fxmlPath Ruta del archivo FXML al que queremos ir
     * @param titulo Título que tendrá la nueva ventana
     */
    public static void cambiarEscena(Control nodoReferencia, String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Util.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElse(nodoReferencia.getScene().getWindow());

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error crítico al cambiar de escena hacia: " + fxmlPath);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la siguiente pantalla.");
        }
    }
    /**
     * Valida si los campos obligatorios del formulario de movimientos están completos.
     * @param concepto Texto del concepto.
     * @param cantidadStr Texto de la cantidad.
     * @param responsable Texto del responsable.
     * @param fecha Objeto de fecha seleccionado.
     * @param tipo Tipo de movimiento seleccionado.
     * @param categoria Categoría seleccionada.
     * @return true si todos los campos son válidos (no vacíos ni nulos), false si falta alguno.
     */
    public static boolean validarCamposMovimiento(String concepto, String cantidadStr, String responsable, LocalDate fecha, String tipo, Categoria categoria) {
        boolean esValido = false;
        if (concepto != null && !concepto.trim().isEmpty() && cantidadStr != null && !cantidadStr.trim().isEmpty() && responsable != null && !responsable.trim().isEmpty() && fecha != null && tipo != null && categoria != null) {
            esValido = true;
        }
        return esValido;
    }
    /**
     * Cierra la ventana actual a partir de cualquiera de sus componentes visuales.
     * @param nodoCualquiera Un componente de la pantalla (un botón, un campo de texto, etc.)
     */
    public static void cerrarVentanaDesde(Node nodoCualquiera) {
        if (nodoCualquiera != null && nodoCualquiera.getScene() != null) {
            Stage stage = (Stage) nodoCualquiera.getScene().getWindow();
            stage.close();
        }
    }

}
