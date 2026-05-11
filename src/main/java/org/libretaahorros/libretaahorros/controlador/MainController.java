package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.libretaahorros.libretaahorros.model.Movimiento;

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
    }
}
