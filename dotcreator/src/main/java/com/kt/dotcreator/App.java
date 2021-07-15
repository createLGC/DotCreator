package com.kt.dotcreator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        VBox root;
        try {
            root = (VBox)FXMLLoader.load(getClass().getResource("/com/kt/dotcreator/fxml/MainFrame.fxml"));
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("DotCreator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}