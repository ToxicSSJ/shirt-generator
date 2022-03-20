module sh.toxic.shirt {

    requires static lombok;

    // requires javafx.scene.control;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens sh.toxic.shirt to javafx.fxml;
    opens sh.toxic.shirt.controller to javafx.fxml;

    exports sh.toxic.shirt;

}