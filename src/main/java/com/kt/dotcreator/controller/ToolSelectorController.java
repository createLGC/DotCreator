package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import com.kt.dotcreator.store.ToolType;

/**
 * ツール選択部のコントローラー
 */
public class ToolSelectorController implements Initializable{
    private ToolType toolType = ToolType.Pen;

    public ToolType getToolType(){
        return this.toolType;
    }

    private Consumer<ToolType> onChange;

    /**
     * @see com.kt.dotcreator.controller.MainController#initialize(URL location, ResourceBundle resources)
     */
    public void setOnChange(Consumer<ToolType> onChange){
        this.onChange = onChange;
    }

    private void setToolType(ToolType toolType){
        if(this.onChange != null)
            this.onChange.accept(toolType);
        this.toolType = toolType;
    }
    

    @FXML
    private Button pen;

    @FXML
    private Button eraser;

    @FXML
    private Button stamp;

    /**
     * ボタンの機能の設定。押すと背景色が変わり、this.setToolType(ToolType toolType)が実行される
     */
    public void initialize(URL location, ResourceBundle resources){
        Button[] buttons = {this.pen, this.eraser, this.stamp};

        this.pen.setOnAction(e->{
            this.setToolType(ToolType.Pen);
            for(Button btn: buttons){btn.setStyle("");}
            this.pen.setStyle("-fx-background-color: #aaa");
        });
        this.eraser.setOnAction(e->{
            this.setToolType(ToolType.Eraser);
            for(Button btn: buttons){btn.setStyle("");}
            this.eraser.setStyle("-fx-background-color: #aaa");
        });
        this.stamp.setOnAction(e->{
            this.setToolType(ToolType.Stamp);
            for(Button btn: buttons){btn.setStyle("");}
            this.stamp.setStyle("-fx-background-color: #aaa");
        });

        this.pen.setStyle("-fx-background-color: #aaa");
    }
}
