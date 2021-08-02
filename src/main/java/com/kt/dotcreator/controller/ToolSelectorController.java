package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.kt.dotcreator.store.ToolType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * ツール選択部のコントローラー
 */
public class ToolSelectorController implements Initializable{
	/**
	 * 選択されているツールタイプ。
	 */
    private ToolType toolType = ToolType.Pen;

    public ToolType getToolType(){
        return this.toolType;
    }
    
    /**
     * ツールタイプが変更されたときの処理のコールバック変数。
     */
    private Consumer<ToolType> onChange;

    /**
     * 上のコールバックを設定。
     * @see com.kt.dotcreator.controller.MainController#initialize(URL location, ResourceBundle resources)
     */
    public void setOnChange(Consumer<ToolType> onChange){
        this.onChange = onChange;
    }
    
    /**
     * ツールタイプを設定。{@link this#onChange.accept(ToolType toolType)}を呼び出し。
     * @param toolType
     */
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
     * ボタンの機能の設定。押すと背景色が変わり、{@link this#setToolType(ToolType toolType)}が実行される
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
