package org.libretaahorros.libretaahorros.dataAccess;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class XMLManager {
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