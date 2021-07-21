package com.kt.dotcreator.store;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.kt.dotcreator.component.LayerCanvas;
import com.kt.dotcreator.controller.EditTabController;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * プロジェクトの情報を持つ
 */
public class Project {
    private String title;
    private int numOfSquaresASide;
    private int squareSize;
    private int zoomRate;
    private ToolType toolType;
    private Color color;

    public String getTitle(){return this.title;}
    public void setTitle(String title){
    	this.title = title;
    	if(this.editTabController != null)
    		this.editTabController.getRoot().setText(title);
    }
    public int getNumOfSquaresASide(){return this.numOfSquaresASide;}
    public void setNumOfSquaresASide(int numOfSquaresASide){this.numOfSquaresASide = numOfSquaresASide;}
    public int getSquareSize(){return this.squareSize;}
    public void setSquareSize(int squareSize){this.squareSize = squareSize;}
    public int getZoomRate(){return this.zoomRate;}
    public void setZoomRate(int zoomRate){
    	this.zoomRate = zoomRate;
    	if(this.editTabController != null)
    		this.editTabController.resize();
    }
    public ToolType getToolType(){return this.toolType;}
    public void setToolType(ToolType toolType){this.toolType = toolType;}
    public Color getColor(){return this.color;}
    public void setColor(Color color){this.color = color;}

    /**
     * @see com.kt.dotcreator.controller.EditTabController#init(Project project)
     * EditTabController.fieldの辺のサイズ。
     */
    public int getCanvasSize(){
        return this.squareSize * this.zoomRate * this.numOfSquaresASide;
    }

    /**
     * @see com.kt.dotcreator.store.Project#saveImage()
     * 出力する画像のサイズ。
     */
    public int getImageSize(){
        return this.squareSize * this.numOfSquaresASide;
    }

    /**
     * 表示されているcanvasの一ブロックのサイズ。
     */
    public int getZoomedSquareSize(){
        return this.squareSize * this.zoomRate;
    }

    private EditTabController editTabController;
    private ArrayList<Layer> layerList = new ArrayList<>();
    /**
     * 現在選択されているレイヤー
     */
    private Layer currentLayer;

    public ArrayList<Layer> getLayerList(){return this.layerList;}
    public Layer getCurrentLayer(){return this.currentLayer;}
    public void setCurrentLayer(Layer layer){this.currentLayer = layer;}
    
    private static final String EDIT_TAB_FXML_PATH = "/com/kt/dotcreator/fxml/EditTab.fxml";

    /**
     * @param tabPane editTabを追加するためにMainControllerから取得
     * editTabをtabPaneに追加し、選択された状態にする。editTabを閉じたときにプロジェクトファイルを保存。
     */
    public Tab loadEditTab() throws IOException {
        FXMLLoader editTabLoader = new FXMLLoader(this.getClass().getResource(EDIT_TAB_FXML_PATH));
        Tab editTab = (Tab) editTabLoader.load();
        this.editTabController = editTabLoader.getController();
        this.editTabController.init(this);
        
        return editTab;
    }

    /**
     * プロジェクトを新規作成.。
     * editTabをロードし、tabPaneに追加。Layerを一つ追加。
     * @param numOfSquaresASide PicturePropertySetterから取得
     * @param squareSize PicturePropertySetterから取得
     * @param tabPane editTabを追加するためにMainControllerから取得
     * @param layerPanel layerを追加するためにMainControllerから取得
     */
    public Project(int numOfSquaresASide, int squareSize){
        this.title = "新規プロジェクト";
        this.numOfSquaresASide = numOfSquaresASide;
        this.squareSize = squareSize;
        this.zoomRate = 10;
        this.toolType = ToolType.Pen;
        this.color = Color.rgb(0, 0, 0, 1);
    }

    /**
     * ファイルからロードされたProjectDataから生成。
     * editTabをtabPaneに追加。layerDataからlayerを生成。
     * @param data ProjectData
     * @param tabPane TabPane
     * @param layerPanel LayerPanel
     */
    public Project(ProjectData data, VBox layerPanel){
        this.title = data.getTitle();
        this.numOfSquaresASide = data.getNumOfSquaresASide();
        this.squareSize = data.getSquareSize();
        this.zoomRate = data.getZoomRate();
        this.toolType = data.getToolType();
        this.color = data.getColorData().getColor();

        layerPanel.getChildren().clear();
        for(LayerData layerData: data.getLayerDataList()){
            this.addLayer(new Layer(this, layerData), layerPanel);
        }
    }

    public ProjectData createProjectData(){
        return new ProjectData(this);
    }

    /**
     * レイヤーを追加
     * @see com.kt.dotcreator.store.Layer
     * @see com.kt.dotcreator.controller.EditTabController#addLayer(LayerCanvas canvas)
     */
    public void addLayer(Layer layer, VBox layerPanel){
        layerPanel.getChildren().add(layer.getLabel());
        this.editTabController.addLayer(layer.getCanvas());
        this.layerList.add(layer);
        layer.setOnDelete(()->{
        	layerPanel.getChildren().remove(layer.getLabel());
        	this.editTabController.getField().getChildren().remove(layer.getCanvas());
        	this.layerList.remove(layer);
        });
    }

    /**
     * レイヤーを新規作成して追加。
     * @param layerPanel LayerPanel
     */
    public void addLayer(VBox layerPanel) {
        this.addLayer(new Layer(this), layerPanel);
    }
    
    public void toggleGrid() {
    	this.editTabController.toggleGrid();
    }

    /**
     * プロジェクトファイルを保存
     */
    public void saveFile() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle( "ファイル保存" );
    	fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) );
    	fileChooser.setInitialFileName(this.title + ".json");
    	File file= fileChooser.showSaveDialog(new Stage());
    	try{
    		if(file == null) return;
    		file.createNewFile();
    		FileWriter writer = new FileWriter(file);
    		writer.write(JsonHandler.encodeProject(this.createProjectData()));
    		writer.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }

    /**
     * 画像を保存
     */
    public void saveImage(){
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle( "ファイル保存" );
    	fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) );
    	fileChooser.setInitialFileName(this.title + ".png");
    	File file= fileChooser.showSaveDialog(new Stage());
        try{
        	if(file == null) return;
            file.createNewFile();
            WritableImage wi = this.editTabController.snapshot(new WritableImage(this.getImageSize(), this.getImageSize()));
            RenderedImage ri = SwingFXUtils.fromFXImage(wi, null);
            ImageIO.write(ri, "png", file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
