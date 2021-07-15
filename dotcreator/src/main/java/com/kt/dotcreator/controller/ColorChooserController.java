package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * 色選択部のコントローラー
 */
public class ColorChooserController implements Initializable{

    private Color color;

    private Consumer<Color> onColorChange;

    /**
     * @see com.kt.dotcreator.controller.MainController.initialize(URL location, ResourceBundle resources)
     */
    public void setOnColorChange(Consumer<Color> onColorChange){
        this.onColorChange = onColorChange;
    }

    @FXML
    private Rectangle colorSample;

    @FXML
    private TextField colorCodeField;

    @FXML
    private HBox rChooser;

    @FXML
    private RgbaChooserController rChooserController;

    @FXML
    private HBox gChooser;

    @FXML
    private RgbaChooserController gChooserController;

    @FXML
    private HBox bChooser;

    @FXML
    private RgbaChooserController bChooserController;

    @FXML
    private HBox aChooser;

    @FXML
    private RgbaChooserController aChooserController;

    private RgbaChooserController[] rgbaChooserControllers;

    /**
     * Colorインスタンスから0~255の値のrgbaの配列を作成
     * @param color Color
     */
    private int[] getRgba(Color color){
        return new int[]{
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255),
            (int) (color.getOpacity() * 255)
        };
    }

    /**
     * 0~255のrgbaの配列から16進数カラーコードを作成
     * @param rgba RGBAの配列
     */
    private String getColorCode(int[] rgba){
        String code = "#";
        for(int i = 0; i < 3; i++){
            String value = Integer.toHexString(rgba[i]).toUpperCase();
            code += value.length() == 1 ? "0" + value : value;
        }
        return code;
    }

    /**
     * プロジェクト生成時に使用
     * @param color Color
     */
    public void init(Color color){
        this.color = color;
        this.colorSample.setFill(this.color);
        int[] rgba = this.getRgba(color);
        this.colorCodeField.setText(this.getColorCode(rgba));
        for(int i = 0; i < 4; i++){
            this.rgbaChooserControllers[i].setValue(rgba[i]);
        }
    }

    private void setColor(int[] rgba){
        this.color = Color.rgb(rgba[0], rgba[1], rgba[2], rgba[3] / 255.0);
        this.colorSample.setFill(this.color);
        if(this.onColorChange != null)    
            this.onColorChange.accept(this.color);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        this.rgbaChooserControllers = new RgbaChooserController[]{
            this.rChooserController,
            this.gChooserController,
            this.bChooserController,
            this.aChooserController
        };

        for(int i = 0; i < this.rgbaChooserControllers.length; i++){
            final int index = i;
            this.rgbaChooserControllers[index].setOnChange((newVal, oldVal)->{
                int[] rgba = new int[4];
                for(int j = 0; j < 4; j++)
                    rgba[j] = j == index ? newVal : this.rgbaChooserControllers[j].getValue();
                this.colorCodeField.setText(this.getColorCode(rgba));
                this.setColor(rgba);
            });
        }

        this.rChooserController.init("R", 0);
        this.gChooserController.init("G", 0);
        this.bChooserController.init("B", 0);
        this.aChooserController.init("A", 255);

        this.colorCodeField.setOnAction(e->{
            String code = this.colorCodeField.getText();
            if(code.matches("^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$")){
                int[] rgba = new int[4];
                final int numOfDigit = code.length() / 3;
                for(int i = 0; i < 3; i++){
                    int index = i * numOfDigit + 1;
                    rgba[i] = Integer.parseInt(code.substring(index, index + numOfDigit), 16);
                    if(numOfDigit == 1){
                        rgba[i] *= 16;
                    }
                    this.rgbaChooserControllers[i].setValue(rgba[i]);
                }
                rgba[3] = aChooserController.getValue();

                this.setColor(rgba);
            }
        });
    }
}
