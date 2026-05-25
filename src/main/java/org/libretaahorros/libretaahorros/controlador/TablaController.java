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
import org.libretaahorros.libretaahorros.model.Gasto;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Esta clase gobierna la pantalla principal de la aplicación, encargándose de orquestar
 * la vinculación de datos en la TableView, el cálculo dinámico de saldos mediante el uso de
 * polimorfismo dinámico y la seguridad basada en roles (RBAC) para limitar funciones críticas.
 * Centraliza las operaciones CRUD fundamentales de las transacciones y sirve como eje de
 * navegación hacia formularios auxiliares modales y selectores de contexto.
 */
public class TablaController {

    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento, String> colFecha;
    @FXML private TableColumn<Movimiento, String> colConcepto;
    @FXML private TableColumn<Movimiento, String> colCategoria;
    @FXML private TableColumn<Movimiento, Double> colCantidad;
    @FXML private TableColumn<Movimiento, String> colTipo;
    @FXML private TableColumn<Movimiento, String> colResponsable;
    @FXML private Button btnEliminar;
    @FXML private Label lblSaldo;

    private ObservableList<Movimiento> obsMovimientos;
    /**
     * Configura el mapeo de datos bidireccional entre las columnas de la TableView y las
     * propiedades de la clase Modelo, invoca el refresco inicial de los movimientos y
     * aplica un control de seguridad por niveles de acceso (RBAC) inhabilitando el botón
     * de eliminación si el usuario actual interactúa bajo el rol de "Invitado".
     */
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
        String rol = SesionController.getRolActual();
        if (rol != null && rol.equalsIgnoreCase("Invitado")) {
            btnEliminar.setDisable(true);
        }
    }

    /**
     * Metodo que recupera los movimientos de la libreta activa desde la base de datos, actualiza la tabla de la interfaz
     * grafica y recalcula el saldo total acumulado. Procesa la coleccion en un unico bucle para discriminar entre ingresos
     * y gastos, actualizando posteriormente el saldo de la libreta tanto en la vista como de forma persistente en el DAO.
     */
    private void cargarMovimientos() {
        Libreta libretaActual = SesionController.getLibreta();
        if (libretaActual == null) {
            return;
        }

        List<Movimiento> listaBD = MovimientoDAO.getInstance().findAllByLibretaEagle(libretaActual.getIdLibreta());
        obsMovimientos = FXCollections.observableArrayList(listaBD);
        tablaMovimientos.setItems(obsMovimientos);

        double totalIngresos = 0.0;
        double totalGastos = 0.0;

        for (Movimiento m : listaBD) {
            if (m instanceof Ingreso) {
                totalIngresos += m.getCantidad();
            } else if (m instanceof Gasto) {
                totalGastos += m.getCantidad();
            }
        }

        double saldoCalculado = totalIngresos - totalGastos;
        lblSaldo.setText(String.format("%.2f €", saldoCalculado));
        LibretaDAO.getInstance().updateSaldo(libretaActual.getIdLibreta(), saldoCalculado);
    }

    /**
     * Metodo con el que el usuario cierra la sesion devolviendolo a la ventana del login.
     */
    @FXML
    private void accionCerrarSesion() {
        SesionController.cerrarSesion();
        Util.cambiarEscena(tablaMovimientos,"/org/libretaahorros/libretaahorros/login_view.fxml", "Iniciar Sesión");
    }

    /**
     * Redirige al usuario de vuelta al panel selector de libretas de ahorro.
     */
    @FXML
    private void accionSalirAlSelector() {
        Util.cambiarEscena( tablaMovimientos,"/org/libretaahorros/libretaahorros/selectorLibreta.fxml", "Mis Libretas");
    }

    /**
     * Metodo para vaciar por completo todos los registros de la libreta activa. Usa modal de confirmacion para prevenir
     * eliminaciones accidentales. Si el usuario confirma la accion, invoca al DAO para ejecutar el borrado masico en
     * cascada en la base de datos y procede a refrescar la interfaz y a recalcular el saldo de esta.
     */
    @FXML
    private void accionBorrarTodoMovimientos() {
        Libreta libretaActual = SesionController.getLibreta();
        if (libretaActual == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar vaciado");
        alert.setHeaderText("¿Borrar todos los movimientos?");
        alert.setContentText("Se eliminará todo el historial de esta libreta, reiniciando su saldo.");

        aplicarEstiloBoton(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean exito = MovimientoDAO.getInstance().deleteAllByLibreta(libretaActual.getIdLibreta());
                if (exito) {
                    cargarMovimientos();
                }
            }
        });
    }

    /**
     * Gestiona la modificación del nombre de la libreta que se encuentra activa en la sesión.
     * Despliega un cuadro de diálogo interactivo (TextInputDialog) precargado con el nombre actual.
     * Si el usuario introduce un nuevo valor válido y no vacío, actualiza el registro en la
     * base de datos a través del DAO, refresca el estado en memoria de la sesión y redirige
     * automáticamente al usuario de vuelta al selector de libretas.
     */
    @FXML
    private void accionEditarNombreLibreta() {
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

            if (!nombreLimpio.isEmpty()) {
                System.out.println("Intentando actualizar libreta ID: " + libretaActual.getIdLibreta() + " a nuevo nombre: " + nombreLimpio);
                boolean exito = LibretaDAO.getInstance().updateNombreLibreta(libretaActual.getIdLibreta(), nombreLimpio);

                if (exito) {
                    libretaActual.setNombre(nombreLimpio);
                    SesionController.setLibreta(libretaActual);
                    Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El nombre de la libreta se ha actualizado correctamente.");
                    accionSalirAlSelector();
                } else {
                    Util.mostrarAlerta(Alert.AlertType.ERROR, "Error de actualización",
                            "No se pudo modificar el nombre en la Base de Datos.\nRevisa si el ID (" + libretaActual.getIdLibreta() + ") es correcto.");
                }
            } else {
                Util.mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "El nombre de la libreta no puede estar vacío.");
            }
        });
    }

    /**
     * Gestiona la eliminación permanente y en cascada de la libreta activa en la sesión.
     * Despliega un cuadro de diálogo modal de confirmación (CONFIRMATION) para advertir
     * al usuario sobre la pérdida irreversible de los datos. Si se confirma la acción,
     * invoca al metodo de borrado del DAO pasándole el identificador único y, tras un
     * borrado exitoso, redirige de forma automática al usuario hacia el selector de libretas.
     */
    @FXML
    private void accionEliminarLibretaActual() {
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
                    accionSalirAlSelector();
                }
            }
        });
    }
    /**
     * Despliega un cuadro de diálogo flotante y modal para el registro de un nuevo movimiento.
     * Inicializa un escenario secundario (Stage) e inhabilita síncronamente la interacción
     * con la ventana principal de fondo (APPLICATION_MODAL) mediante el metodo showAndWait().
     * Una vez que el usuario finaliza la inserción de la transacción y se destruye la ventana
     * emergente, el flujo se reanuda llamando automáticamente a cargarMovimientos() para
     * actualizar la tabla y el saldo reflejado en la interfaz.
     */
    @FXML
    private void accionNuevoMovimiento() {
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
    /**
     * Gestiona la edición de un movimiento seleccionado previamente en la tabla.
     * Verifica la existencia de una selección activa para prevenir excepciones por nulos.
     * Posteriormente, carga la vista del formulario de movimientos y recupera su controlador
     * de forma dinámica para inyectarle síncronamente el objeto a modificar. Despliega la
     * ventana en modo modal (APPLICATION_MODAL) y suspende el flujo hasta su cierre,
     * desencadenando finalmente el refresco de los datos en la interfaz.
     */
    @FXML
    private void accionEditarMovimiento() {
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

    /**
     * Gestiona la eliminación individual del movimiento seleccionado en la tabla.
     * Recupera el registro activo del modelo de selección de la TableView y valida su
     * existencia mediante una cláusula de guarda. Despliega un cuadro de diálogo modal
     * de confirmación (CONFIRMATION) y, en caso de aceptación por parte del usuario,
     * delega en el Singleton del DAO la eliminación del registro en la base de datos
     * mediante su identificador único, procediendo finalmente a refrescar la interfaz.
     */
    @FXML
    private void accionEliminarMovimiento() {
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
    /**
     * Despliega un cuadro de diálogo informativo de tipo 'Acerca de' (INFORMATION)
     * que contiene los metadatos técnicos de la aplicación.
     * Muestra los créditos de autoría del proyecto, la versión actual del software,
     * los datos académicos institucionales de la desarrolladora y una breve descripción
     * funcional de los módulos del sistema.
     */
    @FXML
    private void accionAcercaDe() {
        String mensaje = "Libreta de Ahorros Familiar v1.0\n\n"
                + "Desarrollado por: Cristina Cívico Ariza\n"
                + "Curso: 1º Desarrollo de Aplicaciones Multiplataforma (DAM)\n"
                + "Centro: I.E.S. Francisco de los Ríos\n"
                + "Año: 2026\n\n"
                + "Aplicación de gestión financiera con control de ingresos, gastos, "
                + "múltiples libretas y cálculo de saldos optimizado mediante polimorfismo dinámico.";

        Util.mostrarAlerta(Alert.AlertType.INFORMATION, "Acerca de - Libreta de Ahorros", mensaje);
    }

    /**
     * Personaliza la apariencia visual y altera el comportamiento del foco de los
     * botones en un cuadro de diálogo para optimizar la experiencia de usuario (UX).
     * <p>
     * Inyecta de forma dinámica la hoja de estilos CSS de la aplicación en el DialogPane
     * de la alerta. Adicionalmente, localiza los componentes de confirmación y cancelación
     * para invertir el botón predeterminado por defecto, asignando el foco inicial y la
     * acción de la tecla 'Intro' al botón de cancelación como medida de seguridad ante
     * pulsaciones accidentales.
     * @param alert La instancia de tipo Alert a la que se le aplicarán las modificaciones de estilo y comportamiento.
     */
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
