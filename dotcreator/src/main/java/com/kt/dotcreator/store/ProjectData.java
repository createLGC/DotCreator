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

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumOfSquaresASide() {
		return numOfSquaresASide;
	}

	public void setNumOfSquaresASide(int numOfSquaresASide) {
		this.numOfSquaresASide = numOfSquaresASide;
	}

	public int getSquareSize() {
		return squareSize;
	}

	public void setSquareSize(int squareSize) {
		this.squareSize = squareSize;
	}

	public int getZoomRate() {
		return zoomRate;
	}

	public void setZoomRate(int zoomRate) {
		this.zoomRate = zoomRate;
	}

	public ToolType getToolType() {
		return toolType;
	}

	public void setToolType(ToolType toolType) {
		this.toolType = toolType;
	}

	public ColorData getColorData() {
		return colorData;
	}

	public void setColorData(ColorData colorData) {
		this.colorData = colorData;
	}

	public ArrayList<LayerData> getLayerDataList() {
		return layerDataList;
	}

	public void setLayerDataList(ArrayList<LayerData> layerDataList) {
		this.layerDataList = layerDataList;
	}

    /**
     * Serialize用のデフォルトコンストラクタ
     */
    public ProjectData(){}

    /**
     * プロジェクトを保存するとき
     * @param project 保存するプロジェクト
     */
    public ProjectData(Project project){
        this.title = project.getTitle();
        this.numOfSquaresASide = project.getNumOfSquaresASide();
        this.squareSize = project.getSquareSize();
        this.zoomRate = project.getZoomRate();
        this.toolType = project.getToolType();
        this.colorData = new ColorData(project.getColor());
        for(Layer layer: project.getLayerList()){
            this.layerDataList.add(new LayerData(layer));
        }
    }
}
