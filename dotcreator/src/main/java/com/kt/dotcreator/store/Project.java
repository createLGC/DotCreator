package com.kt.dotcreator.store;

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
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    public void setTitle(String title){this.title = title;}
    public int getNumOfSquaresASide(){return this.numOfSquaresASide;}
    public void setNumOfSquaresASide(int value){this.numOfSquaresASide = value;}
    public int getSquareSize(){return this.squareSize;}
    public void setSquareSize(int value){this.squareSize = value;}
    public int getZoomRate(){return this.zoomRate;}
    public void setZoomRate(int value){this.zoomRate = value;}
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
    private void loadEditTab(TabPane tabPane){
        FXMLLoader editTabLoader = new FXMLLoader(this.getClass().getResource(EDIT_TAB_FXML_PATH));
        try{
            Tab editTab = (Tab) editTabLoader.load();
            editTab.setOnClosed(e->this.saveFile());
            tabPane.getTabs().add(editTab);
            tabPane.getSelectionModel().select(editTab);
            this.editTabController = editTabLoader.getController();
            this.editTabController.init(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * プロジェクトを新規作成.。
     * editTabをロードし、tabPaneに追加。Layerを一つ追加。
     * @param numOfSquaresASide PicturePropertySetterから取得
     * @param squareSize PicturePropertySetterから取得
     * @param tabPane editTabを追加するためにMainControllerから取得
     * @param layerPanel layerを追加するためにMainControllerから取得
     */
    public Project(int numOfSquaresASide, int squareSize, TabPane tabPane, VBox layerPanel){
        this.title = "新規プロジェクト";
        this.numOfSquaresASide = numOfSquaresASide;
        this.squareSize = squareSize;
        this.zoomRate = 10;
        this.toolType = ToolType.Pen;
        this.color = Color.rgb(0, 0, 0, 1);

        this.loadEditTab(tabPane);
        layerPanel.getChildren().clear();
        this.addLayer(layerPanel);
    }

    /**
     * ファイルからロードされたProjectDataから生成。
     * editTabをtabPaneに追加。layerDataからlayerを生成。
     * @param data ProjectData
     * @param tabPane TabPane
     * @param layerPanel LayerPanel
     */
    public Project(ProjectData data, TabPane tabPane, VBox layerPanel){
        this.setTitle(data.getTitle());
        this.setNumOfSquaresASide(data.getNumOfSquaresASide());
        this.setSquareSize(data.getSquareSize());
        this.setZoomRate(data.getZoomRate());
        this.setToolType(data.getToolType());
        this.setColor(data.getColorData().getColor());

        this.loadEditTab(tabPane);
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
    }

    /**
     * レイヤーを新規作成して追加。
     * @param layerPanel LayerPanel
     */
    public void addLayer(VBox layerPanel) {
        this.addLayer(new Layer(this), layerPanel);
    }

    /**
     * プロジェクトファイルを保存
     */
    public void saveFile(){
        try{
            File file = new File(System.getProperty("user.home") + "/" + this.title + ".json");
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
        var wi = new WritableImage(this.getImageSize(), this.getImageSize());
        this.editTabController.snapshot(wi);
        var ri = SwingFXUtils.fromFXImage(wi, null);
        var f = new File(System.getProperty("user.home") + "/" + this.title + ".png");
        try{
            f.createNewFile();
            ImageIO.write(ri, "png", f);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
