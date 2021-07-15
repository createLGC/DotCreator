package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * RGBAを選択するときのスライダーとテキストフィールドのコントローラー
 */
public class RgbaChooserController implements Initializable{

    /**
     * RGBAの0~255までの値
     */
    private int value = 0;

    public int getValue(){
        return this.value;
    }

    /**
     * 外からvalueを変更する関数。valueを変更するとスライダーとテキストフィールドの値が変わる
     */
    public void setValue(int value){
        this.onChange.accept(value, this.value);
        this.slider.setValue(value);
        this.field.setText("" + value);
        this.value = value;
    }

    private BiConsumer<Integer, Integer> onChange;

    /**
     * @see com.kt.dotcreator.controller.ColorChooserController#initialize(URL location, ResourceBundle resources)
     * @param onChange colorChooserController#initializeで設定
     */
    public void setOnChange(BiConsumer<Integer, Integer> onChange){
        this.onChange = onChange;
    }

    @FXML
    private Label label;

    @FXML
    private Slider slider;

    @FXML
    private TextField field;

    public void init(String labelChar, int value){
        this.label.setText(labelChar);
        this.setValue(value);
    }

    /**
     * スライダーの値の変更とテキストフィールドの値の変更を同期させる。ただしうまく動かない。
     */
    public void initialize(URL location, ResourceBundle resources){
        this.slider.valueProperty().addListener((
            ObservableValue<? extends Number> obsv,
            Number newVal,
            Number oldVal
        )->{
            int value = (int) newVal.doubleValue();
            this.onChange.accept(value, this.value);
            this.field.setText("" + value);
            this.value = value;
        });

        this.field.setOnAction(e->{
            try{
                int value = Integer.parseInt(field.getText());
                if(value > 255 || value < 0)
                    throw new NumberFormatException("out of range 0 to 255");
                this.onChange.accept(value, this.value);
                this.slider.setValue(value);
                this.value = value;
            }catch(NumberFormatException ex){
                this.field.setText("" + this.value);
                return;
            }
        });
    }

}
