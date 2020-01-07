/**
 * File header for GuiStreamline.java
 * Description: This file creates a graphical user interface for streamline
 * name: Yuanhao Zhu
 */
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.util.Duration;

/**
 * Class header:
 * This class implements a graphical user interface for streamline
 */
public class GuiStreamline extends Application {
    static final double MAX_SCENE_WIDTH = 600;
    static final double MAX_SCENE_HEIGHT = 600;
    static final double PREFERRED_SQUARE_SIZE = 100;
    static final double LONELY_SQUARE_COORD = 75;
    static final double MIDDLE_OFFSET = 0.5;
    static final double SCALE_TIME = 175;  // milliseconds for scale animation
    static final double FADE_TIME = 250;   // milliseconds for fade animation
    static final double DOUBLE_MULTIPLIER = 2;

    static final String TITLE = "Snake Streamline Game";
    static final String USAGE = 
        "Usage: \n" + 
        "> java GuiStreamline               - to start a game with default" +
        " size 6*5 and random obstacles\n" + 
        "> java GuiStreamline <filename>    - to start a game by reading g" +
        "ame state from the specified file\n" +
        "> java GuiStreamline <directory>   - to start a game by reading a" +
        "ll game states from files in\n" +
        "                                     the specified directory and " +
        "playing them in order\n";

    static final Color TRAIL_COLOR = Color.PALEVIOLETRED;
    static final Color GOAL_COLOR = Color.MEDIUMAQUAMARINE;
    static final Color OBSTACLE_COLOR = Color.DIMGRAY;
    static final Color ZAPPER_COLOR = Color.YELLOW;
    // Trail radius will be set to this fraction of the size of a board square.
    static final double TRAIL_RADIUS_FRACTION = 0.1;

    // Squares will be resized to this fraction of the size of a board square.
    static final double SQUARE_FRACTION = 0.8;
    static final double TRIANGLE_FRACTION = 0.8;
    final static char PLAYER_CHAR = '@';
    final static char GOAL_CHAR = 'G';
    final static char SPACE_CHAR = ' ';
    final static char TRAIL_CHAR = '+';
    final static char OBSTACLE_CHAR = 'O';
    final static char NEWLINE_CHAR = '\n';
    final static char HORIZONTAL_BORDER_CHAR = '-';
    final static char SIDE_BORDER_CHAR = '|';
    final static char DOWN_ZAP_CHAR = 'v';
    final static char UP_ZAP_CHAR = '^';
    final static char LEFT_ZAP_CHAR = '<';
    final static char RIGHT_ZAP_CHAR = '>';

    Stage mainStage;
    Scene mainScene;
    Group levelGroup;
    // For obstacles and trails
    Group rootGroup;
    // Parent group for everything else
    Player playerRect;
    // GUI representation of the player
    RoundedSquare goalRect;
    // GUI representation of the goal

    Shape[][] grid;
    // Same dimensions as the game board
    Shape[][] trailsGrid;
    Streamline game;
    // The current level
    ArrayList<Streamline> nextGames;
    // Future levels

    MyKeyHandler myKeyHandler;
    // for keyboard input

    /** 
     * Coverts the given board column and row into scene coordinates.
     * Gives the center of the corresponding tile.
     * @param boardCol a board column to be converted to a scene x
     * @param boardRow a board row to be converted to a scene y
     * @return scene coordinates as length 2 array where index 0 is x
     */
    public double[] boardIdxToScenePos (int boardCol, int boardRow) {
        double sceneX = ((boardCol + MIDDLE_OFFSET) * 
                (mainScene.getWidth() - 1)) / getBoardWidth();
        double sceneY = ((boardRow + MIDDLE_OFFSET) * 
                (mainScene.getHeight() - 1)) / getBoardHeight();
        return new double[]{sceneX, sceneY};
    }

    /**
     * This method returns the width of board
     * @return the width of board
     */
    public int getBoardWidth() {
        return this.game.currentState.board[0].length;
    }

    /** 
     * This method return the height of board
     * @return the height of board
     */
    public int getBoardHeight() {
        return this.game.currentState.board.length;
    }

    /**
     * This method returns the size of the board
     * @return The best fit size of a square
     */
    public double getSquareSize() {
        //divide the width and height of scene by the number of squares
        double Width_divided = this.mainScene.getWidth()/this.getBoardWidth();
        double Height_divided = this.mainScene.getHeight()/this.getBoardHeight();
        //Return the smaller dimension between width and height
        if(Width_divided > Height_divided) return Width_divided;
        else return Height_divided;
    }

    /** 
     * This method resets all the trails and obstacles and zappers contained in the grid
     */
    public void resetGrid() {
        this.levelGroup.getChildren().clear();
        int boardWidth = this.getBoardWidth();
        int boardHeight = this.getBoardHeight();
        this.grid = new Shape[boardHeight][boardWidth];
        this.trailsGrid= new Shape[boardHeight][boardWidth];
        //Calculate the size of roundedSquare
        double rdSquareSize = this.getSquareSize()*SQUARE_FRACTION;
        //Calculate the size of circle
        double circleSize = this.getSquareSize()*TRAIL_RADIUS_FRACTION;
        //Calculate size of triangle
        double triangleSize = this.getSquareSize() * TRIANGLE_FRACTION;
        for(int i=0; i<boardHeight; i++){
            for(int j=0; j<boardWidth; j++){
                //Calculate the location of center of square or shape based its
                //corresponding location on board.
                double[] pixelCoord = this.boardIdxToScenePos(j,i);
                double x = pixelCoord[0];
                double y = pixelCoord[1];
                char boardchar = this.game.currentState.board[i][j];

                // add each trial circle. and all are initially set to be transparent
                this.trailsGrid[i][j] = new Circle(x,y,circleSize);
                this.trailsGrid[i][j].setFill(Color.TRANSPARENT);
                levelGroup.getChildren().add(this.trailsGrid[i][j]);
                //Create roundedSquare at the locations of obstacles
                if(boardchar == OBSTACLE_CHAR){
                    this.grid[i][j] = new RoundedSquare(x, y, rdSquareSize);
                    this.grid[i][j].setFill(OBSTACLE_COLOR);
                    levelGroup.getChildren().add(this.grid[i][j]);
                }
                // add zapper
                else if(boardchar == DOWN_ZAP_CHAR){
                        this.grid[i][j] = new ZapperTriangle(Direction.DOWN, x, y,triangleSize);
                        this.grid[i][j].setFill(ZAPPER_COLOR);
                        levelGroup.getChildren().add(this.grid[i][j]);

                }
                else if(boardchar == UP_ZAP_CHAR){
                    this.grid[i][j] = new ZapperTriangle(Direction.UP, x, y,triangleSize);
                    this.grid[i][j].setFill(ZAPPER_COLOR);
                    levelGroup.getChildren().add(this.grid[i][j]);

                }
                else if(boardchar == LEFT_ZAP_CHAR){
                    this.grid[i][j] = new ZapperTriangle(Direction.LEFT, x, y,triangleSize);
                    this.grid[i][j].setFill(ZAPPER_COLOR);
                    levelGroup.getChildren().add(this.grid[i][j]);

                }
               else  if(boardchar == RIGHT_ZAP_CHAR){
                    this.grid[i][j] = new ZapperTriangle(Direction.RIGHT, x, y,triangleSize);
                    this.grid[i][j].setFill(ZAPPER_COLOR);
                    levelGroup.getChildren().add(this.grid[i][j]);

                }
                //Create circles at locations of trails or spaces
                else if(boardchar == SPACE_CHAR){
                    this.grid[i][j] = new Circle(x,y,circleSize);
                    this.grid[i][j].setFill(Color.TRANSPARENT);
                    levelGroup.getChildren().add(this.grid[i][j]);
                }
                else if(boardchar == TRAIL_CHAR){
                    this.grid[i][j] = new Circle(x,y,circleSize);
                    this.grid[i][j].setFill(TRAIL_COLOR);
                    levelGroup.getChildren().add(this.grid[i][j]);
                }
                else if(boardchar == PLAYER_CHAR){
                    this.grid[i][j] = new Circle(x,y,circleSize);
                    this.grid[i][j].setFill(Color.TRANSPARENT);
                    levelGroup.getChildren().add(this.grid[i][j]);
                }
            }
        }
        updateTrailColors();
    }

    /**
     * This method fill color of all trail Circles, making them visible or not
     * depending on if that board position equals TRAIL_CHAR
     */
    public void updateTrailColors() {
        //Loop through all elements in grid
        for(int i=0; i<this.grid.length; i++){
            for(int j=0; j<this.grid[0].length; j++){
                // the char on the player board @ . < etc.
                char boardchar = this.game.currentState.board[i][j];
                if(grid[i][j] instanceof ZapperTriangle){
                    //Fill the circle if it corresponds to TRAIL_CHAR
                    if(boardchar == TRAIL_CHAR) {
                        trailsGrid[i][j].setFill(TRAIL_COLOR);
                        grid[i][j].setFill(Color.TRANSPARENT);
                    }
                    //Do not fill the circle if it corresponds to SPACE_CHAR
                    else if(boardchar != TRAIL_CHAR){
                        grid[i][j].setFill(ZAPPER_COLOR);
                        trailsGrid[i][j].setFill(Color.TRANSPARENT);
                    }
                }
                if(grid[i][j] instanceof Circle){
                    if(boardchar == TRAIL_CHAR) {
                        trailsGrid[i][j].setFill(TRAIL_COLOR);
                        grid[i][j].setFill(TRAIL_COLOR);
                    }
                    else{
                        trailsGrid[i][j].setFill(Color.TRANSPARENT);
                        grid[i][j].setFill(Color.TRANSPARENT);
                    }

                }
            }
        }
    }

    /**
     * This method is called when the player is moved to  update the grid and
     * the board
     * @param fromCol The column coordinate of previous player position
     * @param fromRow The row coordinate of previous player position
     * @param toCol The column coordinate of updated player position
     * @param toRow The row coordinate of updated player
     */
    public void onPlayerMoved(int fromCol, int fromRow, int toCol, int toRow)
    {
        
        //Call onLevelFinished is level is passed
        if(this.game.currentState.levelPassed==true){
            onLevelFinished();
        }


        //Do nothing if the player does not move
        if(fromCol==toCol && fromRow==toRow) return;
        //Convert board index of player to scene coordinate
        double[] playerPos = boardIdxToScenePos(toCol, toRow);
        updateTrailColors();
        //Update the location of player
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);
    }

    /** 
     * This method identifies which key is pressed, and move the player 
     * accordingly
     * @param keyCode The code of the key being pressed
     */
    void handleKeyCode(KeyCode keyCode)
    {
        int fromCol = game.currentState.playerCol;
        int fromRow = game.currentState.playerRow;
        switch(keyCode) {
            case UP:
                game.recordAndMove(Direction.UP);
                break;  
            case DOWN:
                game.recordAndMove(Direction.DOWN);
                break;
            case LEFT:
                game.recordAndMove(Direction.LEFT);
                break;                      
            case RIGHT:
                game.recordAndMove(Direction.RIGHT);
                break;        
            case U:
                game.undo();
                break; 
            case O:
                game.saveToFile();
                break;
            case Q:
                System.exit(0);
                break;       
            default:
                System.out.println("Possible commands:\n w - up\n " + 
                        "a - left\n s - down\n d - right\n u - undo\n " + 
                        "q - quit level");
                break;
        }
        int toCol = game.currentState.playerCol;
        int toRow = game.currentState.playerRow;

        /**
           Call onPlayerMoved() to update the GUI to reflect the player's 
           movement
           */
        onPlayerMoved(fromCol, fromRow, toCol, toRow);
    }

    /**
     * This nested class handles keyboard input and calls handleKeyCode()
     */
    class MyKeyHandler implements EventHandler<KeyEvent>
    {
        /**
         * This method invokes the handleKeyCode() method when a key is pressed
         */
        public void handle(KeyEvent e)
        {
            handleKeyCode(e.getCode());            
        }
    }

    /**
     * This method updates the UI with game.currentState
     */
    public void onLevelLoaded()
    {
        this.resetGrid();
        double squareSize = getSquareSize() * SQUARE_FRACTION;
        //Update the player position
        double[] playerPos = boardIdxToScenePos(
                game.currentState.playerCol, game.currentState.playerRow
                );
        playerRect.setSize(squareSize);
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);

        //Update the goal position
        double[] goalPos = boardIdxToScenePos(
                game.currentState.goalCol, game.currentState.goalRow
                );
        goalRect.setSize(squareSize);
        goalRect.setCenterX(goalPos[0]);
        goalRect.setCenterY(goalPos[1]);
        goalRect.setFill(GOAL_COLOR);


    }

    /** 
     * Called when the player reaches the goal. Shows the winning animation
     * and loads the next level if there is one.
     */
    public void onLevelFinished() {
        // Clone the goal rectangle and scale it up until it covers the screen

        // Clone the goal rectangle
        Rectangle animatedGoal = new Rectangle(
                goalRect.getX(),
                goalRect.getY(),
                goalRect.getWidth(),
                goalRect.getHeight()
                );
        animatedGoal.setFill(goalRect.getFill());

        // Scope for children
        {
            // Add the clone to the scene
            List<Node> children = rootGroup.getChildren();
            children.add(children.indexOf(goalRect), animatedGoal);
        }

        // Create the scale animation
        ScaleTransition st = new ScaleTransition(
                Duration.millis(SCALE_TIME), animatedGoal
                );
        st.setInterpolator(Interpolator.EASE_IN);

        // Scale enough to eventually cover the entire scene
        st.setByX(DOUBLE_MULTIPLIER * 
                mainScene.getWidth() / animatedGoal.getWidth());
        st.setByY(DOUBLE_MULTIPLIER * 
                mainScene.getHeight() / animatedGoal.getHeight());

        /**
         * This will be called after the scale animation finishes.
         * If there is no next level, quit. Otherwise switch to it and
         * fade out the animated cloned goal to reveal the new level.
         */
        st.setOnFinished(e1 -> {
                /* TODO 
                   check if there is no next game and if so, quit 
                   update the instances variables game and nextGames 
                   to switch to the next level
                   */
                if(nextGames.size()==0){System.exit(0);}
                else{ 
                    game = nextGames.get(0);
                    nextGames.remove(0);
                }

                //Remove the animation that covers the interface
                this.rootGroup.getChildren().remove(animatedGoal);
 

                // DO NOT MODIFY ANYTHING BELOW THIS LINE IN THIS METHOD

                // Update UI to the next level, but it won't be visible yet
                // because it's covered by the animated cloned goal
                onLevelLoaded();


                Rectangle fadeRect = new Rectangle(0, 0, 
                    mainScene.getWidth(), mainScene.getHeight());
                fadeRect.setFill(goalRect.getFill());

                // Scope for children
                {
                    // Add the fading rectangle to the scene
                    List<Node> children = rootGroup.getChildren();
                    children.add(children.indexOf(goalRect), fadeRect);
                }

                FadeTransition ft = new FadeTransition(
                        Duration.millis(FADE_TIME), fadeRect
                        );
                ft.setFromValue(1);
                ft.setToValue(0);

                // Remove the cloned goal after it's finished fading out
                ft.setOnFinished(e2 -> {
                        rootGroup.getChildren().remove(fadeRect);
                        });

                // Start the fade-out now
                ft.play();
        });

        // Start the scale animation
        st.play();
    }


    /** 
     * Performs file IO to populate game and nextGames using filenames from
     * command line arguments.
     */
    public void loadLevels() {
        game = null;
        nextGames = new ArrayList<Streamline>();

        List<String> args = getParameters().getRaw();
        if (args.size() == 0) {
            System.out.println("Starting a default-sized random game...");
            game = new Streamline();
            return;
        }

        // at this point args.length == 1

        File file = new File(args.get(0));
        if (!file.exists()) {
            System.out.printf("File %s does not exist. Exiting...", 
                    args.get(0));
            return;
        }

        // if is not a directory, read from the file and start the game
        if (!file.isDirectory()) {
            System.out.printf("Loading single game from file %s...\n", 
                    args.get(0));
            game = new Streamline(args.get(0));
            return;
        }

        // file is a directory, walk the directory and load from all files
        File[] subfiles = file.listFiles();
        Arrays.sort(subfiles);
        for (int i=0; i<subfiles.length; i++) {
            File subfile = subfiles[i];

            // in case there's a directory in there, skip
            if (subfile.isDirectory()) continue;

            // assume all files are properly formatted games, 
            // create a new game for each file, and add it to nextGames
            System.out.printf("Loading game %d/%d from file %s...\n",
                    i+1, subfiles.length, subfile.toString());
            nextGames.add(new Streamline(subfile.toString()));
        }

        // Switch to the first level
        game = nextGames.get(0);
        nextGames.remove(0);
    }

    /**
     * The main entry point for all JavaFX Applications
     * Initializes instance variables, creates the scene, and sets up the UI
     * @param  primaryStage The window for this application
     * @throws Exception    [description]
     */
    public void start(Stage primaryStage) throws Exception {
        // Populate game and nextGames
        loadLevels();

        //Initialize the scene and our groups
        rootGroup = new Group();
        mainScene = new Scene(rootGroup, MAX_SCENE_WIDTH, MAX_SCENE_HEIGHT, 
                Color.GAINSBORO);
        //initalize and add level group
        levelGroup = new Group();
        rootGroup.getChildren().add(levelGroup);
        //initialize and add player
        this.playerRect = new Player();
        rootGroup.getChildren().add(this.playerRect);
        //initialize and add goal square
        this.goalRect = new RoundedSquare();
        rootGroup.getChildren().add(this.goalRect);




        //Implement event handler
        this.myKeyHandler = new MyKeyHandler();
        this.mainScene.setOnKeyPressed(myKeyHandler);

        onLevelLoaded();

        // Make the scene visible
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.show();      
    }

    /**
     * Execution begins here, but at this point we don't have a UI yet
     * The only thing to do is call launch() which will eventually result in
     * start() above being called.
     * @param args The arguments from the command line. Should be one 
     *             argument at most which would be the level/directory to load.
     */
    public static void main(String[] args) {
        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }

        launch(args);
    }
}
