package com.kt.dotcreator.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.kt.dotcreator.component.PicturePropertySetter;
import com.kt.dotcreator.store.JsonHandler;
import com.kt.dotcreator.store.Layer;
import com.kt.dotcreator.store.Project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
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
    MenuItem exportImage;

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
                this.currentProject = new Project(
                    numOfSquaresASide, squareSize, this.tabPane, this.layerPanel
                );
                this.colorChooserController.init(this.currentProject.getColor());
                this.projectList.add(this.currentProject);
            });
        });

        this.openProject.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("ファイル選択");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(new Stage());
            try(
                BufferedReader br = new BufferedReader(new FileReader(file));
            ){
                StringBuilder buffer = new StringBuilder();
                String line;
                while((line = br.readLine()) != null){buffer.append(line);}
                this.currentProject = new Project(
                    JsonHandler.decodeProject(buffer.toString()),
                    this.tabPane, this.layerPanel
                );
                this.colorChooserController.init(this.currentProject.getColor());
                this.projectList.add(this.currentProject);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });

        this.exportImage.setOnAction(e->{
            if(this.currentProject != null)
                this.currentProject.saveImage();
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
