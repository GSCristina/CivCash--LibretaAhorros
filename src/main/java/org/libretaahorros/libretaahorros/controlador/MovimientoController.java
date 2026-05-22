package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import org.libretaahorros.libretaahorros.DAO.MovimientoDAO;
import org.libretaahorros.libretaahorros.model.Categoria;
import org.libretaahorros.libretaahorros.model.Gasto;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.utils.Util;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Esta clase gobierna el cuadro de diálogo modal encargado del alta y modificación de transacciones.
 * Implementa una arquitectura versátil basada en herencia y polimorfismo dinámico que permite reutilizar
 * la misma vista para bifurcar lógicas de negocio específicas (Ingresos o Gastos). Recopila atributos
 * extendidos dinámicamente mediante diálogos emergentes síncronos y gestiona de forma transparente
 * la inyección de estados cuando se activa en modalidad de edición.
 */
public class MovimientoController {

    @FXML private TextField txtConcepto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private Button btnCancelar;
    @FXML private TextField txtResponsable;

    private Movimiento movimientoAEditar = null;

    /**
     * Inicializa los componentes de la interfaz de usuario al cargar la vista. Configura las opciones de los desplegables "ComboBox"
     * de tipos y categorias, y establece la fecha actual por defecto en el selector de fecha.
     */
    @FXML
    public void initialize() {
        cbTipo.getItems().addAll("Ingreso", "Gasto");
        cbCategoria.getItems().addAll(Categoria.values());
        dpFecha.setValue(LocalDate.now());
    }

    /**
     * Metodo que se encarga de guardar un movimiento en el sistema, delegando la validacion de los campos obligatorios en la clase "Util"
     * y la recogida de datos especificos mediante dialogos modales. Finalmente, valida si se trata de una insercion o una modificacion para
     * la persistencia del objeto a traves del DAO.
     */
    @FXML
    private void accionGuardar() {
        String concepto = txtConcepto.getText();
        String cantidadStr = txtCantidad.getText();
        LocalDate fecha = dpFecha.getValue();
        String tipo = cbTipo.getValue();
        Categoria categoria = cbCategoria.getValue();
        String responsable = txtResponsable.getText();

        if (!Util.validarCamposMovimiento(concepto, cantidadStr, responsable, fecha, tipo, categoria)) {
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
            String procedencia = pedirProcedenciaIngreso();
            nuevoMovimiento = new Ingreso(0, concepto.trim(), cantidad, fecha, categoria, idLibreta, responsable.trim(), procedencia);
        } else {
            String metodoPago = pedirMetodoPagoGasto();
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
    /**
     * Muestra un cuadro de diálogo interactivo para capturar la procedencia de un ingreso.
     * @return La procedencia introducida por el usuario, u "Otros" por defecto si cancela.
     */
    private String pedirProcedenciaIngreso() {
        TextInputDialog dialog = new TextInputDialog("Nómina");
        dialog.setTitle("Procedencia del Ingreso");
        dialog.setHeaderText("¿De dónde proviene este ingreso?");
        dialog.setContentText("Procedencia:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("Otros");
    }

    /**
     * Muestra un cuadro de diálogo interactivo para capturar el metodo de pago de un gasto.
     * @return El metodo de pago introducido por el usuario, o "Tarjeta" por defecto si cancela.
     */
    private String pedirMetodoPagoGasto() {
        TextInputDialog dialog = new TextInputDialog("Tarjeta");
        dialog.setTitle("Método de Pago");
        dialog.setHeaderText("¿Cómo has pagado este gasto?");
        dialog.setContentText("Método:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("Tarjeta");
    }

    /**
     * Metodo que carga los datos de un movimiento existente en los campos del formulario para poder editarlo.
     * Detecta de forma automatica si se trata de un ingreso o un gasto para ajustar el desplegable.
     * @param m El objeto movimiento (Ingreso o Gasto) que se va a editar
     */
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

    /**
     * Gestiona el evento de cancelación de la pantalla al pulsar el botón correspondiente.
     * Delega la destrucción de la vista en el metodo de cierre interno.
     */
    @FXML
    private void accionCancelar() {
        cerrarVentana();
    }
    /**
     * Recupera el escenario ("Stage") actual de JavaFX a través del botón cancelar
     * y procede a cerrar la ventana de forma segura.
     */
    private void cerrarVentana() {
        Util.cerrarVentanaDesde(btnCancelar);
    }

}
