package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PicturePropertySetterController implements Initializable{
    @FXML
    TextField numOfSquaresASideField;

    @FXML
    TextField squareSizeField;

    @FXML
    Button button;

    private BiConsumer<Integer, Integer> onSubmit;

    public void setOnSubmit(BiConsumer<Integer, Integer> onSubmit){
        this.onSubmit = onSubmit;
    }

    public void initialize(URL location, ResourceBundle resources){
        this.button.setOnAction(e->{
            try{
                this.onSubmit.accept(
                    Integer.parseInt(this.numOfSquaresASideField.getText()),
                    Integer.parseInt(this.squareSizeField.getText())
                );
            }catch(NumberFormatException ex){
                ex.printStackTrace();
            }
        });
    }
}
