package com.kt.dotcreator.store;

import java.io.IOException;

import com.kt.dotcreator.component.LayerCanvas;
import com.kt.dotcreator.controller.LayerLabelController;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * @see com.kt.dotcreator.controller.LayerLabelController
 * @see com.kt.dotcreator.component.LayerCanvas
 * 
 * レイヤー情報をまとめるクラス。
 * LayerLabel, LayerLabelController, LayerCanvasを持つ。
 * LayerLabelを{@value LAYER_LABEL_FXML_PATH}から取得。
 * 
 */
public class Layer {
	/**
	 * LayerLabel.fxmlのパス。
	 */
	private static final String LAYER_LABEL_FXML_PATH = "/com/kt/dotcreator/fxml/LayerLabel.fxml";
	
	/**
	 * レイヤーラベルのインスタンス。
	 */
    private VBox label;
    
    /**
     * レイヤーラベルのコントローラー。
     */
    private LayerLabelController labelController;
    
    /**
     * レイヤーキャンバス。
     */
    private LayerCanvas canvas;

    /**
     * 新規レイヤーを作成。<br>
     * labelを{@value LAYER_LABEL_FXML_PATH}から取得。
     * labelクリック時に他のlabelのbackground-colorをデフォルトに戻し、自分のlabelを暗くする。
     * labelControllerを上記から取得。
     * canvasを新しく生成。
     * labelControllerに目のアイコンをクリックしたときに実行される処理である{@link LayerCanvas#toggleVisible()}を渡す。
     * @param project MainController.currentProject
     * @throws IOException
     */
    public Layer(Project project) throws IOException {
    	FXMLLoader labelLoader = new FXMLLoader(this.getClass().getResource(LAYER_LABEL_FXML_PATH));
    	this.label = (VBox) labelLoader.load();
    	this.label.setOnMouseClicked(e->project.setCurrentLayer(this));
    	this.labelController = (LayerLabelController) labelLoader.getController();
    	this.canvas = new LayerCanvas(project);
    	this.labelController.setOnVisibleChange(()->this.canvas.toggleVisible());
    }

    /**
     * ファイルからロード。<br>
     * labelController.initにLayerDataを渡すことでlabel.nameとlabel.visibleを初期化。
     * canvas生成時にLayerDataを渡すことでcanvasを保存したときの状態にする。
     * @param project MainController.currentProject
     * @param data layerData:projectData.layerDataList
     * @throws IOException
     */
    public Layer(Project project, LayerData data) throws IOException {
    	FXMLLoader labelLoader = new FXMLLoader(this.getClass().getResource(LAYER_LABEL_FXML_PATH));
    	this.label = (VBox) labelLoader.load();
    	this.label.setOnMouseClicked(e->project.setCurrentLayer(this));
    	this.labelController = (LayerLabelController) labelLoader.getController();
    	this.labelController.init(data);
    	this.canvas = new LayerCanvas(project, data);
    	this.labelController.setOnVisibleChange(()->this.canvas.toggleVisible());
    }
    
    /**
     * レイヤー削除ボタンを押したときの処理を設定。
     * @param onDelete 処理のコールバック変数
     */
    public void setOnDelete(Runnable onDelete) {
    	this.labelController.setOnDelete(onDelete);
    }
    
    /**
     * レイヤーラベルを取得。
     * @return this.label
     */
    public VBox getLabel(){
        return this.label;
    }
    
    /**
     * レイヤーキャンバスを取得。
     * @return this.canvas
     */
    public LayerCanvas getCanvas(){
        return this.canvas;
    }
    
    /**
     * レイヤー名を取得。
     * @return レイヤー名
     */
    public String getName(){
        return this.labelController.getName();
    }
    
    /**
     * レイヤーの表示・非表示を取得。
     * @return レイヤーの表示・非表示
     */
    public boolean isVisible(){
        return this.labelController.isVisible();
    }
    
    /**
     * レイヤーキャンバスに描画された内容を保持する配列を取得。
     * @return
     */
    public SquareData[][] getContents(){
        return this.getCanvas().getContents();
    }
}
