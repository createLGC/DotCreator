package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import com.kt.dotcreator.store.LayerData;

/**
 * LayerLabelのlabel, fieldのユーザー入力、値変更。レイヤーの表示・非表示をユーザー入力
 */
public class LayerLabelController implements Initializable{
    @FXML
    private Label label;
    
    @FXML
    private TextField field;

    @FXML
    private ImageView buttonImage;
    /**
     * buttonImageを押したときの処理
     */
    private Runnable onVisibleChange;

    public void setOnVisibleChange(Runnable onVisibleChange){
        this.onVisibleChange = onVisibleChange;
    }

    public String getName(){
        return this.label.getText();
    }

    public boolean isVisible(){
        return this.buttonImage.isVisible();
    }

    /**
     * ファイルからロード時にlayerDataで初期化
     * @see com.kt.dotcreator.store.Layer#Layer(Project project, LayerData data)
     * @param layerData LayerData
     */
    public void init(LayerData layerData){
        this.label.setText(layerData.getName());
        this.buttonImage.setVisible(layerData.isVisible());
    }

    public void initialize(URL location, ResourceBundle resources){
        this.field.setManaged(false);
        this.label.setOnMouseClicked(e->{
            boolean doubleClicked = e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2;
            if(!doubleClicked){
                return;
            }
            this.field.setText(this.label.getText());
            this.label.setVisible(false);
            this.label.setManaged(false);
            this.field.setVisible(true);
            this.field.setManaged(true);
        });

        this.field.setOnAction(e->{
            this.label.setText(this.field.getText());
            this.field.setVisible(false);
            this.field.setManaged(false);
            this.label.setVisible(true);
            this.label.setManaged(true);
        });

        this.buttonImage.setOnMouseClicked(e->{
            this.buttonImage.setOpacity(((int) this.buttonImage.getOpacity()) == 0 ? 1 : 0);
            if(this.onVisibleChange != null)
                this.onVisibleChange.run();
        });
    }
}
