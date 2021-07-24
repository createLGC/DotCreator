package com.kt.dotcreator.store;

/**
 * @see com.kt.dotcreator.store.SquareData
 * レイヤー情報を格納するクラス。
 * レイヤー名、表示・非表示、レイヤーに描かれているコンテンツの配列
 */
public class LayerData {
    private String name;
    private boolean visible;

    private SquareData[][] contents;

    public LayerData(){}

    public LayerData(Layer layer){
        this.name = layer.getName();
        this.visible = layer.isVisible();
        this.contents = layer.getContents();
    }

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public boolean isVisible(){return this.visible;}
    public void setVisible(boolean visible){this.visible = visible;}

    public SquareData[][] getContents(){return this.contents;}
}
