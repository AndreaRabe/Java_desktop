module com.example.employeemanagement {
    requires java.sql;
    //requires fontawesomefx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.employeemanagement to javafx.fxml;
    exports com.example.employeemanagement;
}
