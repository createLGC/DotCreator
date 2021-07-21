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
import com.kt.dotcreator.store.Project;
import com.kt.dotcreator.store.ProjectData;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    @FXML 
    MenuItem createNewProject;

    @FXML
    MenuItem openProject;
    
    @FXML
    MenuItem rename;

    @FXML
    MenuItem exportImage;
    
    @FXML
    Slider zoomSlider;
    
    @FXML
    MenuItem toggleGrid;

    @FXML
    HBox toolSelector;

    @FXML
    ToolSelectorController toolSelectorController;

    @FXML
    VBox colorChooser;

    @FXML
    ColorChooserController colorChooserController;

    @FXML
    TabPane tabPane;
    
    private void addTab(Tab editTab) {
    	tabPane.getTabs().add(editTab);
        tabPane.getSelectionModel().select(editTab);
    }

    @FXML
    VBox layerPanel;

    @FXML
    Button addLayerBtn;

    ArrayList<Project> projectList = new ArrayList<>();

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
        this.toolSelectorController.setOnChange(toolType->{
            if(this.currentProject != null)
                this.currentProject.setToolType(toolType);
        });
        this.colorChooserController.setOnColorChange(color->{
            if(this.currentProject != null)
                this.currentProject.setColor(color);
        });

        this.createNewProject.setOnAction(e->{
            new PicturePropertySetter((numOfSquaresASide, squareSize)->{
            	Project project = new Project(numOfSquaresASide, squareSize);
            	try {
            		this.addTab(project.loadEditTab());
            	}catch(IOException ex) {
            		ex.printStackTrace();
            	}
            	this.currentProject = project;
            	layerPanel.getChildren().clear();
            	this.currentProject.addLayer(layerPanel);
            	this.colorChooserController.init(this.currentProject.getColor());
            	this.projectList.add(this.currentProject);
            });
        });

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
                this.currentProject = new Project(projectData, this.layerPanel);
                this.addTab(this.currentProject.loadEditTab());
                this.colorChooserController.init(this.currentProject.getColor());
                this.projectList.add(this.currentProject);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
        
        this.rename.setOnAction(e->{
        	if(this.currentProject == null) return;
        	TextInputDialog iptDlg  = new TextInputDialog();
            iptDlg.setTitle("プロジェクト名を変更");
            iptDlg.setHeaderText(null);
            iptDlg.setContentText("新しいプロジェクト名：");
            Optional<String> result = iptDlg.showAndWait();
            result.ifPresent(value -> this.currentProject.setTitle(value));
        });

        this.exportImage.setOnAction(e->{
            if(this.currentProject != null)
                this.currentProject.saveImage();
        });
        
        this.zoomSlider.valueProperty().addListener((
        	ObservableValue<? extends Number> obsv,
        	Number newVal,
        	Number oldVal
        )->{
        	if(this.currentProject != null)
        		this.currentProject.setZoomRate((int) newVal.doubleValue());
        });
        
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

        this.addLayerBtn.setOnAction(e->{
            if(this.currentProject != null)
                this.currentProject.addLayer(this.layerPanel);
        });
    }
}
