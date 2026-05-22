package org.libretaahorros.libretaahorros.dataAccess;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Entidad de modelo para la configuración y mapeo de las propiedades de conexión a la base de datos.
 * <p>
 * Esta clase actúa como un POJO (Plain Old Java Object) estructurado para ser deserializado
 * de forma automática mediante la API de JAXB (Jakarta XML Binding). Mapea los nodos del archivo
 * XML de configuración para transferir los parámetros del servidor (host, puerto y credenciales)
 * hacia el gestor de conexiones.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
@XmlRootElement(name = "databaseConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties {

    private String host;
    private int port;
    private String dbName;
    private String user;
    private String password;

    /**
     * Obtiene la dirección IP o el nombre de dominio del host del servidor de base de datos.
     * * @return Cadena de texto con la ubicación del servidor (ej: localhost).
     */
    public String getHost() {
        return host;
    }

    /**
     * Obtiene el puerto de escucha configurado para el servicio MySQL.
     * * @return Valor numérico entero del puerto (ej: 3306).
     */
    public int getPort() {
        return port;
    }

    /**
     * Obtiene el nombre del esquema o base de datos relacional a la que se conectará el sistema.
     * * @return Cadena de texto con el nombre de la base de datos.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Obtiene el nombre de usuario (login) autorizado para la sesión en el motor de base de datos.
     * * @return Cadena de texto con el identificador del usuario de MySQL.
     */
    public String getUser() {
        return user;
    }

    /**
     * Obtiene la contraseña de acceso asociada al usuario del sistema de base de datos.
     * * @return Cadena de texto con la clave credencial en formato plano.
     */
    public String getPassword() {
        return password;
    }
}