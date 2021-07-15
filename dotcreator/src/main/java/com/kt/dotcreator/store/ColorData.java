package com.kt.dotcreator.store;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.paint.Color;

public class ColorData extends SquareData{
    public int r;
    public int g;
    public int b;
    public int a;

    @JsonIgnore
    public Color getColor(){
        return Color.rgb(r, g, b, a / 255.0);
    }

    public ColorData(){}

    public ColorData(Color color){
        this.r = (int) (color.getRed() * 255);
        this.g = (int) (color.getGreen() * 255);
        this.b = (int) (color.getBlue() * 255);
        this.a = (int) (color.getOpacity() * 255);
    }
}
