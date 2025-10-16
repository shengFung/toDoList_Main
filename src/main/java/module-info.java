module com.example.todolistsf {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.mail;

    opens com.example.todolistsf to javafx.fxml;
    exports com.example.todolistsf;
}