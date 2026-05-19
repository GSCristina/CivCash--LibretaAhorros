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

public class SelectorLibretasController {

    @FXML private Label lblUsuario;
    @FXML private FlowPane fpLibretas;

    @FXML
    public void initialize() {
        if (SesionController.getUsuario() != null) {
            lblUsuario.setText("Bienvenido, " + SesionController.getUsuario().getEmail());
        }
        cargarLibretas();
    }

    /**
     * Metodo encargado de cargar las libretas de usuarios, cambia el formato para aquellas compartidas entre otros
     * y aplica diferentes roles, usa lmbda para gestionar la transicion en el controlador de sesion.
     */
    private void cargarLibretas() {
        fpLibretas.getChildren().clear();

        int idUser = SesionController.getUsuario().getIdUsuario();
        List<UsuarioLibreta> listaAsociaciones = LibretaDAO.findAllByUsuarioLazy(idUser);

        for (UsuarioLibreta asociacion : listaAsociaciones) {
            Libreta lib = asociacion.getLibreta();
            Button btnLibreta = new Button(lib.getNombre() + "\nSaldo: " + lib.getSaldoActual() + "€");
            btnLibreta.setPrefSize(120, 80);

            if (asociacion.getRol().equals("Invitado")) {
                btnLibreta.setStyle("-fx-border-color: #3caea3; -fx-border-width: 2px;");
            }
            btnLibreta.setOnAction(event -> {
                SesionController.setLibreta(lib);
                cargarVentanaPrincipal();
            });
            fpLibretas.getChildren().add(btnLibreta);
        }
    }

    /**
     * Este método carga la ventana principal el main donde se encuntran las tablas
      */
    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/libretaahorros/libretaahorros/main_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) fpLibretas.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Libreta de Ahorros - " + SesionController.getLibreta().getNombre());
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleNuevaLibreta() {
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
    @FXML
    public void handleCerrarSesion() {
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
