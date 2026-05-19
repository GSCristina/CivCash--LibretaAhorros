package org.libretaahorros.libretaahorros.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.DAO.LibretaDAO;
import org.libretaahorros.libretaahorros.DAO.MovimientoDAO;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento, String> colFecha;
    @FXML private TableColumn<Movimiento, String> colConcepto;
    @FXML private TableColumn<Movimiento, String> colCategoria;
    @FXML private TableColumn<Movimiento, Double> colCantidad;
    @FXML private TableColumn<Movimiento, String> colTipo;
    @FXML private TableColumn<Movimiento, String> colResponsable;
    @FXML private Label lblSaldo;

    private ObservableList<Movimiento> obsMovimientos;

    @FXML
    public void initialize() {
        System.out.println("Cargado correctamente la pantalla de inicio.");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        cargarMovimientos();
    }

    private void cargarMovimientos() {
        Libreta libretaActual = SesionController.getLibreta();
        if (libretaActual != null) {
            List<Movimiento> listaBD = MovimientoDAO.findAllByLibretaEagle(libretaActual.getIdLibreta());

            obsMovimientos = FXCollections.observableArrayList(listaBD);
            tablaMovimientos.setItems(obsMovimientos);
            double totalIngresos = listaBD.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("Ingreso"))
                    .mapToDouble(Movimiento::getCantidad)
                    .sum();

            double totalGastos = listaBD.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("Gasto"))
                    .mapToDouble(Movimiento::getCantidad)
                    .sum();

            double saldoCalculado = totalIngresos - totalGastos;
            lblSaldo.setText(String.format("%.2f €", saldoCalculado));
            LibretaDAO.updateSaldo(libretaActual.getIdLibreta(), saldoCalculado);
        }
    }

    @FXML
    private void handleCerrarSesion() {
        SesionController.cerrarSesion();
        Util.cambiarEscena(tablaMovimientos,"/org/libretaahorros/libretaahorros/login_view.fxml", "Iniciar Sesión");
    }

    @FXML
    private void handleSalirAlSelector() {
        Util.cambiarEscena( tablaMovimientos,"/org/libretaahorros/libretaahorros/selectorLibreta.fxml", "Mis Libretas");
    }

    @FXML
    private void handleBorrarTodoMovimientos() {
        Libreta libretaActual = SesionController.getLibreta();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar vaciado");
        alert.setHeaderText("¿Borrar todos los movimientos?");
        alert.setContentText("Se eliminará todo el historial de esta libreta, reiniciando su saldo.");

        aplicarEstiloBoton(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean exito = MovimientoDAO.deleteAllByLibreta(libretaActual.getIdLibreta());
                if (exito) {
                    cargarMovimientos();
                }
            }
        });
    }

    @FXML
    private void handleEditarLibretaActual() {
        Libreta libretaActual = SesionController.getLibreta();

        if (libretaActual == null) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Error de Selección", "No hay ninguna libreta activa en la sesión.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(libretaActual.getNombre());
        dialog.setTitle("Editar Nombre de Libreta");
        dialog.setHeaderText("Modificar el nombre de la libreta actual");
        dialog.setContentText("Nuevo nombre:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nuevoNombre -> {
            String nombreLimpio = nuevoNombre.trim();

            if (nombreLimpio.isEmpty()) {
                Util.mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "El nombre de la libreta no puede estar vacío.");
                return;
            }

            System.out.println("Intentando actualizar libreta ID: " + libretaActual.getIdLibreta() + " a nuevo nombre: " + nombreLimpio);
            boolean exito = LibretaDAO.updateNombreLibreta(libretaActual.getIdLibreta(), nombreLimpio);

            if (exito) {
                libretaActual.setNombre(nombreLimpio);
                SesionController.setLibreta(libretaActual);
                Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El nombre de la libreta se ha actualizado correctamente.");
                handleSalirAlSelector();
            } else {
                Util.mostrarAlerta(Alert.AlertType.ERROR, "Error de actualización",
                        "No se pudo modificar el nombre en la Base de Datos.\nRevisa si el ID (" + libretaActual.getIdLibreta() + ") es correcto.");
            }
        });
    }

    @FXML
    private void handleEliminarLibretaActual() {
        Libreta libretaActual = SesionController.getLibreta();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Seguro que deseas eliminar '" + libretaActual.getNombre() + "'?");
        alert.setContentText("Esta acción borrará la libreta y todos sus movimientos de forma permanente.");

        aplicarEstiloBoton(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean exito = LibretaDAO.getInstance().delete(libretaActual.getIdLibreta());
                if (exito) {
                    handleSalirAlSelector();
                }
            }
        });
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
            cargarMovimientos();

        } catch (IOException e) {
            System.err.println("No se pudo abrir la ventana de movimientos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditarMovimiento() {
        Movimiento seleccionado = tablaMovimientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un movimiento de la tabla para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/movimiento_view.fxml"));
            Scene scene = new Scene(loader.load());

            MovimientoController controladorSecundario = loader.getController();
            controladorSecundario.setMovimientoAEditar(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Movimiento");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            cargarMovimientos();

        } catch (IOException e) {
            System.err.println("No se pudo abrir la ventana de edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarMovimiento() {
        Movimiento seleccionado = tablaMovimientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Util.mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un movimiento para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Deseas borrar el registro?");
        alert.setContentText("¿Estás seguro de que quieres eliminar este movimiento? Esta acción no se podrá deshacer.");

        aplicarEstiloBoton(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean exito = MovimientoDAO.getInstance().delete(seleccionado.getIdMovimiento());
                if (exito) {
                    cargarMovimientos();
                } else {
                    Util.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el movimiento de la base de datos.");
                }
            }
        });
    }

    @FXML
    private void handleAcercaDe() {
        String mensaje = "Libreta de Ahorros Familiar v1.0\n\n"
                + "Desarrollado por: Cristina Cívico Ariza\n"
                + "Curso: 1º Desarrollo de Aplicaciones Multiplataforma (DAM)\n"
                + "Centro: I.E.S. Francisco de los Ríos\n"
                + "Año: 2026\n\n"
                + "Aplicación de gestión financiera con control de ingresos, gastos, "
                + "múltiples libretas y cálculo de saldos optimizado mediante streams.";

        Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Acerca de - Libreta de Ahorros", mensaje);
    }

    private void aplicarEstiloBoton(Alert alert) {
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/org/libretaahorros/libretaahorros/Tema.css").toExternalForm()
        );

        Button btnAceptar = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        Button btnCancelar = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);

        if (btnAceptar != null && btnCancelar != null) {
            btnAceptar.setDefaultButton(false);
            btnCancelar.setDefaultButton(true);
            btnCancelar.requestFocus();
        }
    }
}
