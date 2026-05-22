package org.libretaahorros.libretaahorros.dataAccess;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Gestor de persistencia en formato XML encargado del análisis y lectura de la configuración del sistema.
 * <p>
 * Esta clase proporciona la lógica para interactuar con los recursos externos de configuración.
 * Utiliza el motor de JAXB (Jakarta XML Binding) para realizar el proceso de deserialización
 * (Unmarshalling), transformando un archivo plano XML en una instancia de objeto en memoria de
 * forma totalmente automatizada.
 * * @author Cristina Cívico Ariza
 * @version 1.0
 */
public class XMLManager {

    /**
     * Carga y deserializa los parámetros de configuración de la base de datos desde el sistema de archivos.
     * <p>
     * Localiza el archivo físico denominado "db_config.xml" en la raíz del proyecto, inicializa el contexto
     * de JAXB referenciando a la clase meta {@link ConnectionProperties} y genera un analizador (Unmarshaller).
     * Traduce el flujo XML abstrayendo el parseo manual y retorna el mapeo de propiedades.
     * * @return Una instancia cargada de tipo {@link ConnectionProperties} con las credenciales del servidor;
     * {@code null} si el archivo no existe, está mal formado o fallan los permisos de lectura de E/S.
     */
    public static ConnectionProperties loadConfig() {
        try {
            File file = new File("db_config.xml");
            JAXBContext context = JAXBContext.newInstance(ConnectionProperties.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return (ConnectionProperties) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}