package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.model.Movimiento;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento, String> colFecha;
    @FXML private TableColumn<Movimiento, String> colConcepto;
    @FXML private TableColumn<Movimiento, String> colCategoria;
    @FXML private TableColumn<Movimiento, Double> colCantidad;
    @FXML private TableColumn<Movimiento, String> colTipo;

    @FXML
    public void initialize() {
        System.out.println("Cargado correctamente la pantalla de inicio.");
    }

    @FXML
    private void handleNuevoMovimiento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/movimiento_view.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Añadir Nuevo Movimiento");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("No se pudo abrir la ventana de movimientos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
