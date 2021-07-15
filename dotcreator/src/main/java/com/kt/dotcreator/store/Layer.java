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
	private static final String LAYER_LABEL_FXML_PATH = "/com/kt/dotcreator/fxml/LayerLabel.fxml";
	
    private VBox label;
    private LayerLabelController labelController;
    private LayerCanvas canvas;

    /**
     * @param project MainController.currentProject
     * 新規レイヤーを作成。<br>
     * labelを{@value LAYER_LABEL_FXML_PATH}から取得。
     * labelクリック時に他のlabelのbackground-colorをデフォルトに戻し、自分のlabelを暗くする。
     * labelControllerを上記から取得。
     * canvasを新しく生成。
     * labelControllerに目のアイコンをクリックしたときに実行される処理である{@code canvas.setVisible(!canvas.isVisible())}を渡す。
     */
    public Layer(Project project){
        FXMLLoader labelLoader = new FXMLLoader(this.getClass().getResource(LAYER_LABEL_FXML_PATH));
        try{
            this.label = (VBox) labelLoader.load();
            this.label.setOnMouseClicked(e->{
                project.setCurrentLayer(this);
                for(Layer layer: project.getLayerList()){layer.getLabel().setStyle("");}
                this.label.setStyle("-fx-background-color: #aaa");
            });
            this.labelController = (LayerLabelController) labelLoader.getController();
            this.canvas = new LayerCanvas(project);
            this.labelController.setOnVisibleChange(()->this.canvas.setVisible(!this.canvas.isVisible()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @param project MainController.currentProject
     * @param data layerData:projectData.layerDataList
     * ファイルからロード。<br>
     * labelController.initにLayerDataを渡すことでlabel.nameとlabel.visibleを初期化。
     * canvas生成時にLayerDataを渡すことでcanvasを保存したときの状態にする。
     */
    public Layer(Project project, LayerData data){
        FXMLLoader labelLoader = new FXMLLoader(this.getClass().getResource(LAYER_LABEL_FXML_PATH));
        try{
            this.label = (VBox) labelLoader.load();
            this.label.setOnMouseClicked(e->{
                project.setCurrentLayer(this);
                for(Layer layer: project.getLayerList()){layer.getLabel().setStyle("");}
                this.label.setStyle("-fx-background-color: #aaa");
            });
            this.labelController = (LayerLabelController) labelLoader.getController();
            this.labelController.init(data);
            this.canvas = new LayerCanvas(project, data);
            this.labelController.setOnVisibleChange(()->this.canvas.setVisible(!this.canvas.isVisible()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public VBox getLabel(){
        return this.label;
    }

    public LayerCanvas getCanvas(){
        return this.canvas;
    }

    public String getName(){
        return this.labelController.getName();
    }

    public boolean isVisible(){
        return this.labelController.isVisible();
    }

    public SquareData[][] getContents(){
        return this.getCanvas().getContents();
    }
}
