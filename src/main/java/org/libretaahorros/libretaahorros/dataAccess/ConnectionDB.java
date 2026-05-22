package org.libretaahorros.libretaahorros.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Gestor centralizado de conexiones para la base de datos relacional del sistema.
 * <p>
 * Esta clase proporciona un punto único de acceso para obtener la conexión física con el
 * servidor MySQL de forma optimizada. Aplica una variante del patrón Singleton sobre el objeto
 * {@link Connection} para evitar la apertura redundante de canales, e integra una carga
 * desacoplada de credenciales dinámicas mediante archivos de configuración XML.
 */
public class ConnectionDB {

    private static Connection connection;
    /**
     * Recupera o inicializa la instancia activa de conexión con el motor de base de datos.
     * <p>
     * Evalúa si el objeto de conexión no ha sido instanciado o si se encuentra clausurado.
     * En dicho escenario, invoca al gestor XML para parsear los parámetros del host, puerto y credenciales,
     * construye dinámicamente la cadena de conexión JDBC y establece de forma segura el enlace físico.
     * @return El objeto {@link Connection} listo para preparar sentencias SQL;
     * {@code null} si la carga de propiedades falló o se produjo una excepción de red en MySQL.
     */
    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                ConnectionProperties props = XMLManager.loadConfig();

                if (props != null) {
                    String url = "jdbc:mysql://" + props.getHost() + ":" + props.getPort() + "/" + props.getDbName();

                    connection = DriverManager.getConnection(url, props.getUser(), props.getPassword());
                    System.out.println("¡Conexión exitosa a la BBDD!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return connection;
    }
}
