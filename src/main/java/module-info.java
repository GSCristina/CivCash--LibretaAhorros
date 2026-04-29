module org.libretaahorros.libretaahorros {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.libretaahorros.libretaahorros to javafx.fxml;
    exports org.libretaahorros.libretaahorros;
}