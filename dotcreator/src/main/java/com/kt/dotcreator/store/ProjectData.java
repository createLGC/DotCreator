package com.kt.dotcreator.store;

import java.util.ArrayList;

/**
 * Projectをこれに変換して保存するクラス
 */
public class ProjectData {
    private String title;
    private int numOfSquaresASide;
    private int squareSize;
    private int zoomRate;
    private ToolType toolType;
    private ColorData colorData;

    private ArrayList<LayerData> layerDataList = new ArrayList<>();

    /**
     * Serialize用のデフォルトコンストラクタ
     */
    public ProjectData(){}

    /**
     * プロジェクトを保存するとき
     * @param project 保存するプロジェクト
     */
    public ProjectData(Project project){
        this.setTitle(project.getTitle());
        this.setNumOfSquaresASide(project.getNumOfSquaresASide());
        this.setSquareSize(project.getSquareSize());
        this.setZoomRate(project.getZoomRate());
        this.setToolType(project.getToolType());
        this.setColorData(new ColorData(project.getColor()));
        for(Layer layer: project.getLayerList()){
            this.layerDataList.add(new LayerData(layer));
        }
    }

    public String getTitle(){return this.title;}
    public void setTitle(String value){this.title = value;}

    public int getNumOfSquaresASide(){ return this.numOfSquaresASide; }
    public void setNumOfSquaresASide(int value){ this.numOfSquaresASide = value; }

    public int getSquareSize(){ return this.squareSize; }
    public void setSquareSize(int value){ this.squareSize = value; }

    public int getZoomRate(){ return this.zoomRate; }
    public void setZoomRate(int value){ this.zoomRate = value; }

    public ToolType getToolType(){ return this.toolType; }
    public void setToolType(ToolType value){ this.toolType = value; }

    public ColorData getColorData(){ return this.colorData; }
    public void setColorData(ColorData value){ this.colorData = value; }

    public ArrayList<LayerData> getLayerDataList(){ return this.layerDataList; }
}
