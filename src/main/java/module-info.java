module com.example.unasatbpconverter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.unasat.bpconconverter.main to javafx.fxml;
    opens com.unasat.bpconconverter.controller to javafx.fxml;
    exports com.unasat.bpconconverter.main;
    exports com.unasat.bpconconverter.controller;
}
