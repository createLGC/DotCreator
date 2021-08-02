package com.kt.dotcreator.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.kt.dotcreator.component.PicturePropertySetter;
import com.kt.dotcreator.store.JsonHandler;
import com.kt.dotcreator.store.Layer;
import com.kt.dotcreator.store.LayerData;
import com.kt.dotcreator.store.Project;
import com.kt.dotcreator.store.ProjectData;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * メインウィンドウのコントローラー。プロジェクトのリストを保持。
 */
public class MainController implements Initializable{
	/**
	 * 新規プロジェクト作成ボタン
	 */
    @FXML 
    MenuItem createNewProject;
    
    /**
     * 既存のプロジェクトを開くボタン
     */
    @FXML
    MenuItem openProject;
    
    /**
     * プロジェクト名変更ボタン
     */
    @FXML
    MenuItem renameProject;
    
    /**
     * プロジェクト保存ボタン
     */
    @FXML
    MenuItem saveProject;
    
    /**
     * 画像出力ボタン
     */
    @FXML
    MenuItem exportImage;
    
    /**
     * キャンバスの表示倍率を変更するスライダー
     */
    @FXML
    Slider zoomSlider;
    
    /**
     * グリッドの表示・非表示を切り替えるボタン
     */
    @FXML
    MenuItem toggleGrid;
    
    /**
     * ツール選択部
     */
    @FXML
    HBox toolSelector;
    
    /**
     * ツール選択部のコントローラー
     */
    @FXML
    ToolSelectorController toolSelectorController;
    
    /**
     * カラー設定部
     */
    @FXML
    VBox colorChooser;
    
    /**
     * カラー設定部のコントローラー
     */
    @FXML
    ColorChooserController colorChooserController;
    
    /**
     * キャンバスを表示するタブペイン。
     */
    @FXML
    TabPane tabPane;
    
    /**
     * タブペインにタブを追加する処理。
     * @param editTab
     * @param project
     */
    private void addTab(Tab editTab, Project project) {
    	this.tabPane.getTabs().add(editTab);
        this.tabPane.getSelectionModel().select(editTab);
        editTab.setOnClosed(e->{
        	this.layerPanel.getChildren().clear();
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("プロジェクトの終了");
        	alert.setHeaderText(null);
        	alert.setContentText("プロジェクトを保存しますか?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK) {
        		project.save();
        	}
        	int index = this.projectList.indexOf(project);
        	int size = this.projectList.size();
        	if(size == 1) {}
        	else if(index == size - 1)
        		this.currentProject = this.projectList.get(index - 1);
        	else
        		this.currentProject = this.projectList.get(index + 1);
        	this.projectList.remove(project);
        });
    }
    
    /**
     * レイヤーラベルを内包するコンテナ。
     */
    @FXML
    VBox layerPanel;
    
    /**
     * 新規レイヤーを追加するボタン。
     */
    @FXML
    Button addLayerBtn;
    
    /**
     * 開いているプロジェクトのリスト
     */
    ArrayList<Project> projectList = new ArrayList<>();
    
    /**
     * 選択されているタブのプロジェクト
     */
    Project currentProject;

    /**
     * toolSelectorControllerの変化時のコールバック、
     * colorChooserControllerの変化時のコールバック、
     * createNewProjectを押したときのコールバック、
     * openProjectを押したときのコールバック、
     * exportImageを押したときのコールバック、
     * tabPaneでTab変更時のコールバック、
     * addLayerBtnを押したときのコールバック
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
    	/**
    	 * ツール選択部で選択されているツールが変わったときの処理を設定。
    	 */
        this.toolSelectorController.setOnChange(toolType->{
            if(this.currentProject != null)
                this.currentProject.setToolType(toolType);
        });
        
        /**
         * カラー設定部で設定されている色が変わったときの処理を設定。
         */
        this.colorChooserController.setOnColorChange(color->{
            if(this.currentProject != null)
                this.currentProject.setColor(color);
        });
        
        /**
         * 新規プロジェクト作成ボタンを押したときの処理を設定。
         */
        this.createNewProject.setOnAction(e->{
            new PicturePropertySetter((numOfSquaresASide, squareSize)->{
            	try {
            		Project project = new Project(numOfSquaresASide, squareSize);
            		this.currentProject = project;
            		this.addTab(this.currentProject.loadEditTab(), project);
            		this.layerPanel.getChildren().clear();
            		this.currentProject.addLayer(layerPanel);
            		this.colorChooserController.init(this.currentProject.getColor());
            		this.projectList.add(this.currentProject);
            	}catch(IOException ex) {
            		ex.printStackTrace();
            	}
            });
        });
        
        /**
         * 既存のプロジェクトを開くボタンを押したときの処理を設定。
         */
        this.openProject.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("ファイル選択");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(new Stage());
            if(file == null) return;
            try(
                BufferedReader br = new BufferedReader(new FileReader(file));
            ){
                StringBuilder buffer = new StringBuilder();
                String line;
                while((line = br.readLine()) != null){buffer.append(line);}
                ProjectData projectData = JsonHandler.decodeProject(buffer.toString());
                Project project = new Project(projectData);
                this.currentProject = project;
                this.addTab(this.currentProject.loadEditTab(), project);
                layerPanel.getChildren().clear();
                for(LayerData layerData: projectData.getLayerDataList()){
                    this.currentProject.addLayer(new Layer(this.currentProject, layerData), layerPanel);
                }
                this.colorChooserController.init(this.currentProject.getColor());
                this.projectList.add(this.currentProject);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
        
        /**
         * プロジェクト名変更ボタンを押したときの処理を設定。
         */
        this.renameProject.setOnAction(e->{
        	if(this.currentProject == null) return;
        	TextInputDialog iptDlg  = new TextInputDialog();
            iptDlg.setTitle("プロジェクト名を変更");
            iptDlg.setHeaderText(null);
            iptDlg.setContentText("新しいプロジェクト名：");
            Optional<String> result = iptDlg.showAndWait();
            result.ifPresent(value -> this.currentProject.setTitle(value));
        });
        
        /**
         * プロジェクト保存ボタンを押したときの処理を設定。
         */
        this.saveProject.setOnAction(e->{
        	if(this.currentProject != null)
        		this.currentProject.save();
        });
        
        /**
         * 画像出力ボタンを押したときの処理を設定。
         */
        this.exportImage.setOnAction(e->{
            if(this.currentProject != null)
                this.currentProject.saveImage();
        });
        
        /**
         * キャンバスの表示倍率を変更するスライダーの値を変更したときの処理を設定。
         */
        this.zoomSlider.valueProperty().addListener((
        	ObservableValue<? extends Number> obsv,
        	Number newVal,
        	Number oldVal
        )->{
        	if(this.currentProject != null)
        		this.currentProject.setZoomRate((int) newVal.doubleValue());
        });
        
        /**
         * グリッドの表示・非表示を切り替えるボタンを押したときの処理を設定。
         */
        this.toggleGrid.setOnAction(e->{
        	if(this.currentProject == null) return;
        	this.currentProject.toggleGrid();
        	switch(this.toggleGrid.getText()) {
        	case "show Grid":
        		this.toggleGrid.setText("hide Grid");
        		break;
        	case "hide Grid":
        		this.toggleGrid.setText("show Grid");
        		break;
        	}
        });
        
        /**
         * タブペインで現在選択されているタブが変わったとき(現在使用中のプロジェクトが変わったとき)の処理を設定
         */
        this.tabPane.getSelectionModel().selectedIndexProperty().addListener((obsv, ov, nv)->{
            final int index = nv.intValue();
            if(index >= 0 && projectList.size() > index){
                this.currentProject = this.projectList.get(index);
                this.colorChooserController.init(this.currentProject.getColor());
                this.layerPanel.getChildren().clear();
                for(Layer layer: this.currentProject.getLayerList()){
                    this.layerPanel.getChildren().add(layer.getLabel());
                }
            }
        });
        
        /**
         * 新規レイヤー追加ボタンを押したときの処理を設定。
         */
        this.addLayerBtn.setOnAction(e->{
        	if(this.currentProject == null) return;
        	try {
        		this.currentProject.addLayer(this.layerPanel);
        	}catch(IOException ex) {
        		ex.printStackTrace();
        	}
        });
    }
}
