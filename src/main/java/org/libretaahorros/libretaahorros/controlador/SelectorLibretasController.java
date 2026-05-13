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

import java.io.IOException;
import java.util.List;

public class SelectorLibretasController {

    @FXML private Label lblUsuario;
    @FXML private FlowPane fpLibretas;

    @FXML
    public void initialize() {
        if (Sesion.getUsuario() != null) {
            lblUsuario.setText("Bienvenido, " + Sesion.getUsuario().getEmail());
        }
        cargarLibretas();
    }

    private void cargarLibretas() {
        fpLibretas.getChildren().clear();

        int idUser = Sesion.getUsuario().getIdUsuario();
        List<Libreta> listaLibretas = LibretaDAO.findAllByUsuario(idUser);

        for (Libreta lib : listaLibretas) {
            Button btnLibreta = new Button(lib.getNombre() + "\nSaldo: " + lib.getSaldoActual() + "€");
            btnLibreta.setPrefSize(120, 80);

            btnLibreta.setOnAction(event -> {
                Sesion.setLibreta(lib);
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
            stage.setTitle("Libreta de Ahorros - " + Sesion.getLibreta().getNombre());
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleNuevaLibreta() {
        System.out.println("Clic en Nueva Libreta. ¡Próximamente!");
    }

    @FXML
    public void handleCerrarSesion() {
        System.out.println("Cerrando sesión...");
        Sesion.cerrarSesion();

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
