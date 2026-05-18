package org.libretaahorros.libretaahorros.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.stage.Window;

public class Util {
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

}
