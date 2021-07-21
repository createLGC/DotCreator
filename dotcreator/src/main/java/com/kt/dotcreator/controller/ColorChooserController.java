package com.kt.dotcreator.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 色選択部のコントローラー
 */
public class ColorChooserController implements Initializable{
	/**
	 * Colorインスタンス
	 */
    private Color color;
    
    /**
     * this.colorが変更されたときに呼び出されるコールバック関数
     */
    private Consumer<Color> onColorChange;

    /**
     * onColorChangeを設定。
     * @see com.kt.dotcreator.controller.MainController.initialize(URL location, ResourceBundle resources)
     */
    public void setOnColorChange(Consumer<Color> onColorChange){
        this.onColorChange = onColorChange;
    }
    
    /**
     * 現在のthis.colorで色を表示する。
     */
    @FXML
    private Rectangle colorSample;
    
    /**
     * 16進数カラーコードを入力する。
     */
    @FXML
    private TextField colorCodeField;
    
    /**
     * 赤色の値を設定する。
     */
    @FXML
    private HBox rChooser;
    
    /**
     * rChooserのコントローラー。
     */
    @FXML
    private RgbaChooserController rChooserController;
    
    /**
     * 緑色の値を設定する。
     */
    @FXML
    private HBox gChooser;
    
    /**
     * gChooserのコントローラー。
     */
    @FXML
    private RgbaChooserController gChooserController;
    
    /**
     * 青色の値を設定する。
     */
    @FXML
    private HBox bChooser;
    
    /**
     * bChooserのコントローラー。
     */
    @FXML
    private RgbaChooserController bChooserController;
    
    /**
     * 透明度の値を設定する。
     */
    @FXML
    private HBox aChooser;
    
    /**
     * aChooserのコントローラー。
     */
    @FXML
    private RgbaChooserController aChooserController;
    
    /**
     * アセットを保存するボタン
     */
    @FXML
    private Button saveAssetBtn;
    
    private final String ASSET_LABEL_PATH = "/com/kt/dotcreator/fxml/AssetLabel.fxml";
    
    /**
     * アセットを表示する
     */
    @FXML
    private VBox assetLabelContainer;
    
    /**
     * 4つのコントローラーをまとめる配列
     */
    private RgbaChooserController[] rgbaChooserControllers;

    /**
     * Colorインスタンスから0～255の値のrgbaの配列を作成
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
     * 0～255のrgbaの配列から16進数カラーコードを作成
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
    
    /**
     * this.colorにrgbaからColorを設定し、colorSampleを変更し、onColorChangeを呼び出す。
     * @param rgba
     */
    private void setColor(int[] rgba){
        this.color = Color.rgb(rgba[0], rgba[1], rgba[2], rgba[3] / 255.0);
        this.colorSample.setFill(this.color);
        if(this.onColorChange != null)    
            this.onColorChange.accept(this.color);
    }
    
    /**
     * 自動で呼び出される初期化メソッド。
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
    	//4つのコントローラーを配列にする。
        this.rgbaChooserControllers = new RgbaChooserController[]{
            this.rChooserController,
            this.gChooserController,
            this.bChooserController,
            this.aChooserController
        };
        
        //4つのコントローラーにonChange時に実行される関数を設定。
        for(int i = 0; i < this.rgbaChooserControllers.length; i++){
            final int index = i;
            this.rgbaChooserControllers[index].setOnChange((newVal, oldVal)->{
                int[] rgba = new int[4];
                //4つのコントローラーすべてから値を取得。
                for(int j = 0; j < 4; j++)
                    rgba[j] = j == index ? newVal : this.rgbaChooserControllers[j].getValue();
                this.colorCodeField.setText(this.getColorCode(rgba));
                this.setColor(rgba);
            });
        }
        
        //4つのコントローラーをそれぞれ初期化
        this.rChooserController.init("R", 0);
        this.gChooserController.init("G", 0);
        this.bChooserController.init("B", 0);
        this.aChooserController.init("A", 255);
        
        //this.colorCodeFieldでEnterが押されたときに実行される関数を設定。
        this.colorCodeField.setOnAction(e->{
            String code = this.colorCodeField.getText();
            if(code.matches("^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$")){
                int[] rgba = new int[4];
                //16進数カラーコードの一つの色の桁数(1つか2つ)を取得
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
        
        //アセットを保存する処理。
        this.saveAssetBtn.setOnAction(e->{
        	FXMLLoader assetLabelLoader = new FXMLLoader(this.getClass().getResource(ASSET_LABEL_PATH));
            try{
                assetLabelLoader.load();
                AssetLabelController controller = (AssetLabelController) assetLabelLoader.getController();
                this.assetLabelContainer.getChildren().add(controller.getRoot());
                controller.init(
                	this.color, 
                	this.getColorCode(this.getRgba(this.color)),
                	event->{
                		int[] rgba = this.getRgba(controller.getColor());
                		this.colorCodeField.setText(this.getColorCode(rgba));
                		for(int i = 0; i < 3; i++){this.rgbaChooserControllers[i].setValue(rgba[i]);}
                		this.setColor(rgba);
                		for(Node node: this.assetLabelContainer.getChildren()){node.setStyle("");}
                		controller.getRoot().setStyle("-fx-background-color: #aaa");
                	},
                	event->{
                		this.assetLabelContainer.getChildren().remove(controller.getRoot());
                	}
                );
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
    }
}
