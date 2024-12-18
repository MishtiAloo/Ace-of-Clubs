module com.example.allah_rohom_koro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.google.gson;
    requires java.net.http;
    requires java.sql;


    opens com.example.allah_rohom_koro to javafx.fxml;
    exports com.example.allah_rohom_koro;
}