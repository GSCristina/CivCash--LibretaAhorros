package org.libretaahorros.libretaahorros.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.libretaahorros.libretaahorros.DAO.LibretaDAO;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.UsuarioLibreta;

import java.io.IOException;
import java.util.List;

/**
 * Esta clase gestiona el panel intermedio post-autenticación, encargado de renderizar
 * de forma dinámica los accesos a las libretas asociadas al usuario en sesión mediante
 * un contenedor de disposición fluida (FlowPane). Implementa una estrategia de carga
 * atenuada o diferida (Lazy) para optimizar las consultas iniciales a la base de datos,
 * y mapea las restricciones de seguridad inyectando el rol correspondiente en el contexto de sesión.
 */
public class SelectorLibretasController {

    @FXML private Label lblUsuario;
    @FXML private FlowPane fpLibretas;

    /**
     * Metodo que muestra una frase de bienvenida al usuario que inicia sesion
     */
    @FXML
    public void initialize() {
        if (SesionController.getUsuario() != null) {
            lblUsuario.setText("¡Es genial volver a verte " + SesionController.getUsuario().getEmail()+ " !");
        }
        cargarLibretas();
    }

    /**
     * Metodo encargado de cargar las libretas de usuarios usando el metodo lazy del DAO para buscar todas
     * las libretas a las que esta asociado el usuario logueado y aplica diferentes roles, usa lambda para
     * gestionar la transicion en el controlador de sesion.
     */
    private void cargarLibretas() {
        fpLibretas.getChildren().clear();

        int idUser = SesionController.getUsuario().getIdUsuario();
        List<UsuarioLibreta> listaAsociaciones = LibretaDAO.getInstance().findAllByUsuarioLazy(idUser);

        for (UsuarioLibreta asociacion : listaAsociaciones) {
            Libreta libreta = asociacion.getLibreta();
            Button btnLibreta = new Button(libreta.getNombre() + "\nSaldo: " + libreta.getSaldoActual() + "€");
            btnLibreta.setPrefSize(120, 80);

            btnLibreta.setOnAction(event -> {
                SesionController.setLibreta(libreta);
                SesionController.setRolActual(asociacion.getRol());
                cargarVentanaPrincipal();
            });
            fpLibretas.getChildren().add(btnLibreta);
        }
    }

    /**
     * Este metodo se encarga de hacer la transicion entre pantallas cerrando la vista del selector de libretas y cargando
     * el panel principal, es decir, la tabla dentro de esta. Utilizamos recursos de java para evitar tener que poner una ruta
     * absoluta.
      */
    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/Tabla_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) fpLibretas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Libreta de Ahorros: " + SesionController.getLibreta().getNombre());
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se encarga de crear una libreta totalmente nueva y actualizando automaticamente la venta anterior
     * para mostrar la nueva escena, utiliza una ventana modal bloqueando la anterior hasta que el usuario decida que termine
     * de crear la nueva libreta o la cancele.
     */
    @FXML
    public void accionNuevaLibreta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/nueva_libreta_view.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Crear Nueva Libreta");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            cargarLibretas();

        } catch (IOException e) {
            System.err.println("No se pudo abrir la ventana de nueva libreta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo con el que el usuario cierra la sesion devolviendolo a la ventana del login.
     */
    @FXML
    public void accionCerrarSesion() {
        System.out.println("Cerrando sesión...");
        SesionController.cerrarSesion();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/login_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) fpLibretas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login - Libreta de Ahorros");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
