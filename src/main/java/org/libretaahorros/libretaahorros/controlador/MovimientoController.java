package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.DAO.MovimientoDAO;
import org.libretaahorros.libretaahorros.model.Categoria;
import org.libretaahorros.libretaahorros.model.Gasto;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.utils.Util;

import java.time.LocalDate;
import java.util.Optional;

public class MovimientoController {

    @FXML private TextField txtConcepto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private Button btnCancelar;
    @FXML private TextField txtResponsable;
    private Movimiento movimientoAEditar = null;

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll("Ingreso", "Gasto");
        cbCategoria.getItems().addAll(Categoria.values());
        dpFecha.setValue(LocalDate.now());
    }

    @FXML
    private void handleGuardar() {
        String concepto = txtConcepto.getText();
        String cantidadStr = txtCantidad.getText();
        LocalDate fecha = dpFecha.getValue();
        String tipo = cbTipo.getValue();
        Categoria categoria = cbCategoria.getValue();
        String responsable = txtResponsable.getText();

        if (concepto == null || concepto.trim().isEmpty() ||
                cantidadStr == null || cantidadStr.trim().isEmpty() ||
                responsable == null || responsable.trim().isEmpty() ||
                fecha == null || tipo == null || categoria == null) {

            Util.mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, rellene todos los campos del formulario.");
            return;
        }
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr.trim());
        } catch (NumberFormatException e) {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "La cantidad debe ser un número válido (ej: 14.50).");
            return;
        }

        int idLibreta = SesionController.getLibreta().getIdLibreta();
        Movimiento nuevoMovimiento;

        if ("Ingreso".equalsIgnoreCase(tipo)) {
            TextInputDialog dialog = new TextInputDialog("Nómina");
            dialog.setTitle("Procedencia del Ingreso");
            dialog.setHeaderText("¿De dónde proviene este ingreso?");
            dialog.setContentText("Procedencia:");
            Optional<String> result = dialog.showAndWait();
            String procedencia = result.orElse("Otros");

            nuevoMovimiento = new Ingreso(0, concepto.trim(), cantidad, fecha, categoria, idLibreta, responsable.trim(), procedencia);
        } else {
            TextInputDialog dialog = new TextInputDialog("Tarjeta");
            dialog.setTitle("Método de Pago");
            dialog.setHeaderText("¿Cómo has pagado este gasto?");
            dialog.setContentText("Método:");
            Optional<String> result = dialog.showAndWait();
            String metodoPago = result.orElse("Tarjeta");

            nuevoMovimiento = new Gasto(0, concepto.trim(), cantidad, fecha, categoria, idLibreta, responsable.trim(), metodoPago);
        }

        boolean exito;
        if (movimientoAEditar != null) {
            nuevoMovimiento.setIdMovimiento(movimientoAEditar.getIdMovimiento());
            exito = MovimientoDAO.getInstance().update(nuevoMovimiento);
        } else {
            exito = MovimientoDAO.getInstance().add(nuevoMovimiento);
        }

        if (exito) {
            cerrarVentana();
        } else {
            Util.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el movimiento.");
        }
    }
    public void setMovimientoAEditar(Movimiento m) {
        this.movimientoAEditar = m;
        txtConcepto.setText(m.getConcepto());
        txtCantidad.setText(String.valueOf(m.getCantidad()));
        dpFecha.setValue(m.getFecha());
        cbCategoria.setValue(m.getCategoria());
        txtResponsable.setText(m.getResponsable());

        if (m instanceof Ingreso) {
            cbTipo.setValue("Ingreso");
        } else {
            cbTipo.setValue("Gasto");
        }
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
