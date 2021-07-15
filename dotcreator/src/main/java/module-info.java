module com.kt.dotcreator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;

    opens com.kt.dotcreator to javafx.fxml;
    opens com.kt.dotcreator.controller to javafx.fxml;
    exports com.kt.dotcreator;
}