package org.libretaahorros.libretaahorros;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión...");
        org.libretaahorros.libretaahorros.dataAccess.ConnectionDB.getInstance();


        Application.launch(HelloApplication.class, args);
    }

}
