package com.kt.dotcreator.component;

import java.io.IOException;
import java.util.function.BiConsumer;

import com.kt.dotcreator.controller.PicturePropertySetterController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PicturePropertySetter extends Stage{
	private static final String FXML_PATH = "/com/kt/dotcreator/fxml/PicturePropertySetter.fxml";
	
    public PicturePropertySetter(BiConsumer<Integer, Integer> callback){
        super();
        FXMLLoader sceneLoader = new FXMLLoader(this.getClass().getResource(FXML_PATH));
        try{
            this.setScene(new Scene((VBox) sceneLoader.load()));
            PicturePropertySetterController controller = sceneLoader.getController();
            controller.setOnSubmit((numOfSquaresASide, squareSize)->{
                callback.accept(numOfSquaresASide, squareSize);
                this.hide();
            });
            this.setTitle("プロジェクト作成");
            this.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
