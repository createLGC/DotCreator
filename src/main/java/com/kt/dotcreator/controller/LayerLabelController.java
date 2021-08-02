package com.kt.dotcreator.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.kt.dotcreator.store.LayerData;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

/**
 * LayerLabelのlabel, fieldのユーザー入力、値変更。レイヤーの表示・非表示をユーザー入力
 */
public class LayerLabelController implements Initializable{
	/**
	 * ルート要素
	 */
	@FXML
	private VBox root;
	
	public VBox getRoot() {
		return this.root;
	}
	
	/**
	 * レイヤー名を表示
	 */
    @FXML
    private Label label;
    
    /**
     * レイヤー名を入力。labelをダブルクリックすると入れ替わりで表示される。
     */
    @FXML
    private TextField field;
    
    /**
     * クリックするとレイヤーの表示・非表示を切り替える
     */
    @FXML
    private ImageView visibleBtn;

    /**
     * レイヤーの表示・非表示を取得
     * @return
     */
    public boolean isVisible(){
        return this.visibleBtn.isVisible();
    }
    
    /**
     * visibleBtnを押したときの処理
     */
    private Runnable onVisibleChange;

    public void setOnVisibleChange(Runnable onVisibleChange){
        this.onVisibleChange = onVisibleChange;
    }
    
    /**
     * レイヤー名を取得
     * @return
     */
    public String getName(){
        return this.label.getText();
    }
    
    /**
     * レイヤーを削除するボタン
     */
    @FXML
    private ImageView deleteBtn;
    
    public void setOnDelete(Runnable onDelete){
    	this.deleteBtn.setOnMouseClicked(e->onDelete.run());
    }

    /**
     * ファイルからロード時にlayerDataで初期化
     * @see com.kt.dotcreator.store.Layer#Layer(Project project, LayerData data)
     * @param layerData LayerData
     */
    public void init(LayerData layerData){
        this.label.setText(layerData.getName());
        this.visibleBtn.setVisible(layerData.isVisible());
    }
    
    /**
     * 自動で呼び出される初期化メソッド。
     */
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

        this.visibleBtn.setOnMouseClicked(e->{
            this.visibleBtn.setOpacity(((int) this.visibleBtn.getOpacity()) == 0 ? 1 : 0);
            if(this.onVisibleChange != null)
                this.onVisibleChange.run();
        });
    }
}
