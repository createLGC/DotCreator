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
	//*************************************************************************
	//
	//	プロジェクト全体で連動する情報
	//
	//-------------------------------------------------------------------
	/**
	 * プロジェクト名
	 */
    private String title;
    
    /**
     * プロジェクト名取得関数
     * @return プロジェクト名
     */
    public String getTitle(){return this.title;}
    
    /**
     * プロジェクト名を設定。同時にEditTabのタイトルも変更。
     * @param title
     */
    public void setTitle(String title){
    	this.title = title;
    	if(this.editTabController != null)
    		this.editTabController.getRoot().setText(title);
    }
    //-------------------------------------------------------------------
    /**
     * キャンバスの一辺に何ブロックあるか。
     */
    private int numOfSquaresASide;
    public int getNumOfSquaresASide(){return this.numOfSquaresASide;}
    public void setNumOfSquaresASide(int numOfSquaresASide){this.numOfSquaresASide = numOfSquaresASide;}
    //-------------------------------------------------------------------
    /**
     * 一ブロックの一辺のサイズ(px)。
     */
    private int squareSize;
    public int getSquareSize(){return this.squareSize;}
    public void setSquareSize(int squareSize){this.squareSize = squareSize;}
    //-------------------------------------------------------------------
    /**
     * キャンバスが画像の何倍の大きさか。
     */
    private int zoomRate;
    public int getZoomRate(){return this.zoomRate;}
    public void setZoomRate(int zoomRate){
    	this.zoomRate = zoomRate;
    	if(this.editTabController != null)
    		this.editTabController.resize();
    }
    //-------------------------------------------------------------------
    /**
     * ツール選択部で現在選択されているツールタイプ。
     */
    private ToolType toolType;
    public ToolType getToolType(){return this.toolType;}
    public void setToolType(ToolType toolType){this.toolType = toolType;}
    //-------------------------------------------------------------------
    /**
     * カラー設定部で現在設定されている色。
     */
    private Color color;
    public Color getColor(){return this.color;}
    public void setColor(Color color){this.color = color;}
    //-------------------------------------------------------------------
    //*************************************************************************
    //
    //	上のプロジェクト情報から計算される値
    //
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
    //*************************************************************************
    /**
     * キャンバスを表示するタブのコントローラー
     */
    private EditTabController editTabController;
    
    /**
     * レイヤーのリスト
     */
    private ArrayList<Layer> layerList = new ArrayList<>();
    public ArrayList<Layer> getLayerList(){return this.layerList;}
    
    /**
     * 現在選択されているレイヤー
     */
    private Layer currentLayer;
    public Layer getCurrentLayer(){return this.currentLayer;}
    /**
     * 引数レイヤーをcurrentLayerに設定。レイヤーラベルの背景色を変更。
     * @param layer
     */
    public void setCurrentLayer(Layer layer){
    	this.currentLayer = layer;
    	for(Layer innerLayer: this.layerList){innerLayer.getLabel().setStyle("");}
        this.currentLayer.getLabel().setStyle("-fx-background-color: #aaa");
    }
    
    /**
     * EditTab.fxmlのパス。
     */
    private static final String EDIT_TAB_FXML_PATH = "/com/kt/dotcreator/fxml/EditTab.fxml";

    /**
     * editTabをロードし、コントローラを取得・初期化。
     * @param tabPane editTabを追加するためにMainControllerから取得
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
     * @param numOfSquaresASide PicturePropertySetterから取得
     * @param squareSize PicturePropertySetterから取得
     * @param tabPane タブペイン
     * @param layerPanel レイヤーラベルのコンテナ
     * @throws IOException
     */
    public Project(int numOfSquaresASide, int squareSize) {
        this.title = "新規プロジェクト";
        this.numOfSquaresASide = numOfSquaresASide;
        this.squareSize = squareSize;
        this.zoomRate = 10;
        this.toolType = ToolType.Pen;
        this.color = Color.rgb(0, 0, 0, 1);
    }

    /**
     * ファイルからロードされたProjectDataから生成。
     * @param data ProjectData
     * @param tabPane タブペイン
     * @param layerPanel レイヤーラベルのコンテナ
     * @throws IOException
     */
    public Project(ProjectData data) {
        this.title = data.getTitle();
        this.numOfSquaresASide = data.getNumOfSquaresASide();
        this.squareSize = data.getSquareSize();
        this.zoomRate = data.getZoomRate();
        this.toolType = data.getToolType();
        this.color = data.getColorData().getColor();
    }
    
    /**
     * ProjectDataを生成。
     * @return
     */
    public ProjectData createProjectData(){
        return new ProjectData(this);
    }

    /**
     * レイヤーを追加
     * @see com.kt.dotcreator.store.Layer
     * @see com.kt.dotcreator.controller.EditTabController#addLayer(LayerCanvas canvas)
     */
    public void addLayer(Layer layer, VBox layerPanel) {
        layerPanel.getChildren().add(layer.getLabel());
        this.editTabController.addLayer(layer.getCanvas());
        this.layerList.add(layer);
        layer.setOnDelete(()->{
        	int index = this.layerList.indexOf(layer);
        	int size = this.layerList.size();
        	if(size == 1)
        		this.currentLayer = null;
        	else if(index == size - 1)
        		this.setCurrentLayer(this.layerList.get(index - 1));
        	else
        		this.setCurrentLayer(this.layerList.get(index + 1));
        	layerPanel.getChildren().remove(layer.getLabel());
        	this.editTabController.getField().getChildren().remove(layer.getCanvas());
        	this.layerList.remove(layer);
        });
        this.setCurrentLayer(layer);
    }

    /**
     * レイヤーを新規作成して追加。
     * @param layerPanel LayerPanel
     */
    public void addLayer(VBox layerPanel) throws IOException {
        this.addLayer(new Layer(this), layerPanel);
    }
    
    /**
     * グリッドの表示・非表示を切り替え。
     */
    public void toggleGrid() {
    	this.editTabController.toggleGrid();
    }

    /**
     * プロジェクトファイルを保存
     */
    public void save() {
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
