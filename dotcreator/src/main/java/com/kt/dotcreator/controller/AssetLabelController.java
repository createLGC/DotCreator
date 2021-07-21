package com.kt.dotcreator.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
	 * 最外要素
	 */
	@FXML
	private HBox root;
	
	public HBox getRoot() {
		return this.root;
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
	
	/**
	 * 削除するボタン
	 */
	@FXML
	private ImageView deleteBtn;
	
	/**
	 * 初期化メソッド。
	 */
	public void init(Color color, String colorCode, EventHandler<MouseEvent> onMouseClicked) {
		this.color = color;
		this.colorSample.setFill(color);
		this.label.setText(colorCode);
		this.root.setOnMouseClicked(onMouseClicked);
		this.deleteBtn.setOnMouseClicked(e->this.root.getParent().getChildrenUnmodifiable().remove(this.root));
	}
}
