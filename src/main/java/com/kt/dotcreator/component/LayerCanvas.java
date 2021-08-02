package com.kt.dotcreator.component;

import com.kt.dotcreator.store.ColorData;
import com.kt.dotcreator.store.ImageData;
import com.kt.dotcreator.store.LayerData;
import com.kt.dotcreator.store.Project;
import com.kt.dotcreator.store.SquareData;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * クリックされたところにコンテンツを描画
 */
public class LayerCanvas extends Canvas{
	/**
	 * 所属するプロジェクト
	 */
    Project project;
    
    /**
     * 描画コンテキスト
     */
    GraphicsContext ctx;
    
    /**
     * ブロックごとに何が描画されているかを保持する配列。
     */
    SquareData[][] contents;

    public SquareData[][] getContents(){return this.contents;}

    /**
     * this.contentsに引数contentsを設定し、contentsにしたがって{@link LayerCanvas#drawContents()}で描画。
     * 既存のプロジェクトを開くときに使用。
     * @param contents LayerDataから取得
     */
    private void setContents(SquareData[][] contents){
        this.contents = contents;
        this.drawContents();
    }
    
    /**
     * this.contentsの要素であるcontentがColorDataのインスタンスの時、Colorを生成し描画。
     * ImageDataのインスタンスの時、Imageを生成し描画。
     */
    public void drawContents() {
    	int squareSize = this.project.getZoomedSquareSize();
        for(int y = 0; y < this.contents.length; y++){
            for(int x = 0; x < this.contents[y].length; x++){
                SquareData content = this.contents[y][x];
                if(content instanceof ColorData){
                    Color color = ((ColorData) content).getColor();
                    this.ctx.setFill(color);
                    this.ctx.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                }else if(content instanceof ImageData){
                    Image image = new Image(((ImageData) content).getURL());
                    this.ctx.drawImage(image, x * squareSize, y * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    /**
     * 新規レイヤー生成時に使用。
     * contentsをSquareDataの配列で初期化。
     * @see com.kt.dotcreator.store.SquareData
     * @param project 新規プロジェクトまたはMainController.currentProject
     */
    public LayerCanvas(Project project){
        super(project.getCanvasSize(), project.getCanvasSize());
        this.project = project;
        this.ctx = this.getGraphicsContext2D();
        this.contents = new SquareData[project.getNumOfSquaresASide()][project.getNumOfSquaresASide()];
    }
    
    /**
     * ファイルからロード時に使用。
     * layerData.visibleから表示・非表示を設定。
     * layerData.contentsからレイヤーを描画。
     * @see com.kt.dotcreator.store.LayerData
     * @param project ロードしてできたプロジェクト
     * @param data ロードしたLayerData
     */
    public LayerCanvas(Project project, LayerData data){
        super(project.getCanvasSize(), project.getCanvasSize());
        this.project = project;
        this.ctx = this.getGraphicsContext2D();
        this.setVisible(data.isVisible());
        this.setContents(data.getContents());
    }
    
    /**
     * layerCanvasの表示・非表示を切り替える。
     */
    public void toggleVisible() {
    	this.setVisible(!this.isVisible());
    }

    /**
     * クリックされた座標を取得しどのブロックか判定。
     * {@link MainController#currentProject#getToolType()}でtoolSelectorで選択されたToolTypeを取得。
     * ToolTypeごとにオブジェクトを生成し描画。LayerCanvas.contentsに描画するオブジェクトをDataにして格納
     * @see com.kt.dotcreator.store.ToolType
     * @param e MouseEvent
     */
    public void drawSquare(MouseEvent e){
        int squareSize = this.project.getZoomedSquareSize();
        int canvasSize = this.project.getCanvasSize();
        int x = (int) e.getX();
        int y = (int) e.getY();
        if(x >= canvasSize || x < 0 || y >= canvasSize || y < 0){
            return;
        }

        x -= x % squareSize;
        y -= y % squareSize;

        switch(this.project.getToolType()){
            case Pen:
                this.ctx.setFill(this.project.getColor());
                this.ctx.fillRect(x, y, squareSize, squareSize);
                this.contents[y / squareSize][x / squareSize] = new ColorData(this.project.getColor());
                break;
            case Eraser:
                this.ctx.clearRect(x, y, squareSize, squareSize);
                this.contents[y / squareSize][x / squareSize] = null;
                break;
            case Stamp:
                break;
        }
    }
}