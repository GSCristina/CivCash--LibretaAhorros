package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MovimientoController {

    @FXML private TextField txtConcepto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll("Ingreso", "Gasto");
        cbCategoria.getItems().addAll("Supermercado", "Ocio", "Vivienda", "Transporte", "Otros", "Impuestos", "Suscripciones");
        dpFecha.setValue(java.time.LocalDate.now());
    }

    @FXML
    private void handleGuardar() {
        System.out.println("--- DATOS DEL NUEVO MOVIMIENTO ---");
        System.out.println("Concepto: " + txtConcepto.getText());
        System.out.println("Cantidad: " + txtCantidad.getText() + "€");
        System.out.println("Fecha: " + dpFecha.getValue());
        System.out.println("Tipo: " + cbTipo.getValue());
        System.out.println("Categoría: " + cbCategoria.getValue());

        cerrarVentana();
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
