module org.libretaahorros.libretaahorros {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.xml.bind;

    opens org.libretaahorros.libretaahorros.model to jakarta.xml.bind, javafx.base;
    opens org.libretaahorros.libretaahorros.controlador to javafx.fxml;


    exports org.libretaahorros.libretaahorros;
    exports org.libretaahorros.libretaahorros.controlador;
}