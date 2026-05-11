package org.libretaahorros.libretaahorros.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static Connection connection;

    public static Connection getInstance() {
        if (connection == null) {
            try {
                ConnectionProperties props = XMLManager.loadConfig();

                if (props != null) {
                    String url = "jdbc:mysql://" + props.getHost() + ":" + props.getPort() + "/" + props.getDbName();

                    connection = DriverManager.getConnection(url, props.getUser(), props.getPassword());
                    System.out.println("¡Conexión exitosa a la base de datos!");
                }
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        return connection;
    }
}
