package com.kt.dotcreator.controller;

import java.util.Optional;

import com.kt.dotcreator.component.LayerCanvas;
import com.kt.dotcreator.store.Layer;
import com.kt.dotcreator.store.Project;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EditTabController {
	@FXML
	private Tab root;
	
	public Tab getRoot() {
		return this.root;
	}
	
    @FXML
    private StackPane field;
    
    public StackPane getField() {
    	return this.field;
    }

    @FXML
    private Canvas gridCanvas;

    private Project project;

    /**
     * project生成時に呼び出し。
     * @see com.kt.dotcreator.store.Project#loadEditTab(TabPane tabPane)
     * @param project 生成されたProject
     */
    public void init(Project project){
        this.project = project;
        int fieldSize = this.project.getCanvasSize(); 
        this.field.setPrefSize(fieldSize, fieldSize);
        this.initGridCanvas(fieldSize);
        this.field.setOnMousePressed(this::draw);
        this.field.setOnMouseDragged(this::draw);
        this.root.setOnClosed(e->{
        	Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("プロジェクトの終了");
            alert.setHeaderText(null);
            alert.setContentText("プロジェクトを保存しますか?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
            	this.project.saveFile();
            }
        });
    }
    
    public void resize() {
    	int fieldSize = this.project.getCanvasSize(); 
        this.field.setPrefSize(fieldSize, fieldSize);
        for(Node child: this.field.getChildren()) {
        	Canvas canvas = (Canvas) child;
        	canvas.setWidth(fieldSize);
        	canvas.setHeight(fieldSize);
        	canvas.getGraphicsContext2D().clearRect(0, 0, fieldSize, fieldSize);
        	if(canvas == this.gridCanvas) {
        		this.initGridCanvas(fieldSize);
        	}else {
        		((LayerCanvas) canvas).drawContents();
        	}
        }
    }

    /**
     * gridCanvasを生成
     * @param fieldSize this.fieldの辺のサイズ
     */
    private void initGridCanvas(int fieldSize){
        this.gridCanvas.setWidth(fieldSize);
        this.gridCanvas.setHeight(fieldSize);

        GraphicsContext ctx = this.gridCanvas.getGraphicsContext2D();
        int zoomedSquareSize = this.project.getZoomedSquareSize();
        for(int i = 1; i < this.project.getNumOfSquaresASide(); i++){
            ctx.strokeLine(i * zoomedSquareSize, 0, i * zoomedSquareSize, fieldSize);
            ctx.strokeLine(0, i * zoomedSquareSize, fieldSize, i * zoomedSquareSize);
        }
    }

    /**
     * gridCanvasの表示・非表示の切り替え
     */
    public void toggleGrid(){
        this.gridCanvas.setVisible(!this.gridCanvas.isVisible());
    }

    /**
     * @see com.kt.dotcreator.store.Project#addLayer(Layer layer, VBox layerPanel)
     * @param canvas LayerCanvas
     */
    public void addLayer(LayerCanvas canvas){
        this.field.getChildren().remove(this.gridCanvas);
        this.field.getChildren().addAll(canvas, this.gridCanvas);
    }

    public void draw(MouseEvent e){
        Layer currentLayer = this.project.getCurrentLayer();
        if(currentLayer != null)
            currentLayer.getCanvas().drawSquare(e);
    }

    /**
     * fieldを本来の画像サイズに縮小し、snapshotを撮影、元の大きさに戻す。
     * @see com.kt.dotcreator.store.Project#saveImage()
     * @param wi WritableImage
     */
    public WritableImage snapshot(WritableImage wi){
        this.field.setScaleX(1.0 / this.project.getZoomRate());
        this.field.setScaleY(1.0 / this.project.getZoomRate());
        this.field.setStyle("-fx-border-width: 0px");
        this.gridCanvas.setVisible(false); 
        this.field.snapshot(null, wi);
        this.field.setScaleX(1.0);
        this.field.setScaleY(1.0);
        this.field.setStyle("-fx-border-color: black;-fx-border-width: 1px");
        this.gridCanvas.setVisible(true);
        
        return wi;
    }
}
