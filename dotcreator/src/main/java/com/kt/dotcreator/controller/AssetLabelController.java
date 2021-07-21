package com.kt.dotcreator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AssetLabelController {
	/**
	 * Colorインスタンス
	 */
	private Color color;
	
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * 色を表示
	 */
	@FXML
	private Rectangle colorSample;
	
	/**
	 * 16進数カラーコードを表示
	 */
	@FXML
	private Label label;
	
	/*
	 * 初期化メソッド。
	 */
	public void init(Color color, String colorCode) {
		this.color = color;
		this.colorSample.setFill(color);
		this.label.setText(colorCode);
	}
}
