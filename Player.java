/**
 * File header for Player.java
 * Description: This class customizes the roundedSquare for player
 * name: Yuanhao Zhu
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

/**
 * This class implements the shape of player
 */
public class Player extends RoundedSquare {
    final static double STROKE_FRACTION = 0.1;
    final static Color PLAYER_FILL_COLOR = Color.BLUE;
    final static Color PLAYER_STROKE_COLOR = Color.PINK;

    /**
     * No-arg constructor for player
     */
    public Player() {
        /* 
         set a fill color, a stroke color, and set the stroke type to
         centered
         */
        super.setFill(PLAYER_FILL_COLOR);
        super.setStroke(PLAYER_STROKE_COLOR);
        super.setStrokeType(StrokeType.CENTERED);
    }
    
    /**
     * This method sets the stroke width
     * @param size The size of player rectangle
     */
    @Override
    public void setSize(double size) {
        /* 
         1. update the stroke width based on the size and 
            STROKE_FRACTION
         2. call super setSize(), bearing in mind that the size
            parameter we are passed here includes stroke but the
            superclass's setSize() does not include the stroke
         */
        super.setStrokeWidth(size*STROKE_FRACTION);
        super.setSize(size-super.getStrokeWidth()); 
    }
}
