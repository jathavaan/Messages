module com.org.messages {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.org.messages to javafx.fxml;
    exports com.org.messages;
}