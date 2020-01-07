/**
 * File header for GameState.java
 * Description: Define the state of the game, contains the rotation of the
 * board and the moveLeft related method. Essential functions to the game is
 * written here.
 * name: Yuanhao Zhu
 */

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.util.*;

/**
 * Class header: GameState
 * Define the state of teh game. The symbols of the game is defined here,
 * such as '@' represent the player, and those symbols are defined as
 * constants. The board, the row and column of the player and the goal, and
 * the levelPassed are also initialized here.
 */
public class GameState {
    /* Provided constants */
    final static char PLAYER_CHAR = '@';
    final static char GOAL_CHAR = 'G';
    final static char SPACE_CHAR = ' ';
    final static char TRAIL_CHAR = '+';
    final static char OBSTACLE_CHAR = 'O';
    final static char DOWN_ZAP_CHAR = 'v';
    final static char UP_ZAP_CHAR = '^';
    final static char LEFT_ZAP_CHAR = '<';
    final static char RIGHT_ZAP_CHAR = '>';
    final static char NEWLINE_CHAR = '\n';
    final static char HORIZONTAL_BORDER_CHAR = '-';
    final static char SIDE_BORDER_CHAR = '|';

    /* Add your `final static` constants here */
    //those private integers are used for the random generator for random
    // zappers.
    private static final int RANDOMNUMFOUR = 4;
    private static final int RANDOMCASETWO = 2;
    private static final int RANDOMCASETHREE = 3;


    /* Instance variables, do not add any */
    char[][] board;
    // 2-D array containing the tiles of the board, each tile has a value
    int playerRow;
    // the row of the playerin the board (0-indexing),
    // access using board[playerRow][playerCol]
    int playerCol; // the column of the player in the board (0-indexing)
    int goalRow; // the row of the goal in the board (0-indexing)
    int goalCol; // the colum of the goal in the board (0-indexing)
    boolean levelPassed; // denotes whether the level is passed

    /**
     * This constructor initialized the game board with given parameters, and
     * fill in the empty game board with SPACE_CHAR. All the instance
     * variables initialized here, and the player and goal are put on the
     * game board.
     *
     * @param height    the height of the game board.
     * @param width     th3 width of the game board.
     * @param playerRow the row the player should be.
     * @param playerCol the column the player should be.
     * @param goalRow   the row the goal should be.
     * @param goalCol   the column the goal should be.
     */
    public GameState(int height, int width, int playerRow, int playerCol,
                     int goalRow, int goalCol) {
        this.board = new char[height][width];
        // fill out the board with empty space char
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.board[i][j] = SPACE_CHAR;
            }
        }
        // initialize all the instance variables
        this.playerCol = playerCol;
        this.playerRow = playerRow;
        this.goalCol = goalCol;
        this.goalRow = goalRow;
        this.board[playerRow][playerCol] = PLAYER_CHAR;
        this.board[goalRow][goalCol] = GOAL_CHAR;
        //check if the game ends
        if (playerCol == goalCol && playerRow == goalRow) {
            this.levelPassed = true;
        } else {
            this.levelPassed = false;
        }
    }

    /**
     * a copy constructor, initialized the game state using the instance
     * variables from other.
     *
     * @param other The gameState object that we want to copy from.
     */
    public GameState(GameState other) {
//copy the other's board
        this.board = new char[other.board.length][other.board[0].length];
        for (int i = 0; i < other.board.length; i++) {
            for (int j = 0; j < other.board[0].length; j++) {
                this.board[i][j] = other.board[i][j];
            }
        }
        // initialize all the instance variables based on other's instance
        // variables
        this.playerCol = other.playerCol;
        this.playerRow = other.playerRow;
        this.goalCol = other.goalCol;
        this.goalRow = other.goalRow;
        this.levelPassed = other.levelPassed;

    }

    /**
     * main method to test the functionality. Need to pass for the check point.
     *
     * @param args what we typed on the keyboard
     */
    public static void main(String[] args) {
        GameState state = new GameState(10, 1, 1,
                0, 4, 0);
        //create new game board
        GameState state2 = new GameState(10, 10, 9,
                0, 0, 9);
        GameState state3 = new GameState(state2);
        state3.levelPassed = true;
        System.out.print(state.toString());//print out the state
        state.rotateCounterClockwise();//rotate the game board.
        System.out.print(state.toString());//print our the state
    }

    /**
     * run through the this.board and count the number of empty tiles.
     *
     * @return the number of empty tiles in this.board
     */
    int countEmptyTiles() {
        //initialized the count
        int count = 0;
        // loop through each row and col and count number of space char
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.board[i][j] == SPACE_CHAR) {
                    count = count + 1;
                }
            }
        }
        //return the number of empty tiles
        return count;
    }

    /**
     * add count number of obstacles at random position of the board, but do
     * not overwrite if that tiles is not empty.
     *
     * @param count the number of obstacles we want to add
     */
    void addRandomObstacles(int count) {
        // check if the number of obstacles exceeds the available spaces
        int num = countEmptyTiles();
        if (count > num || count < 0) {
            return;
        }
        // create random generater
        Random r = new Random();
        int remain = count;
        // add the obstacles to the board
        while (remain > 0) {
            int randrow = r.nextInt(this.board.length);
            int randcol = r.nextInt(this.board[0].length);
            if (this.board[randrow][randcol] == SPACE_CHAR) {
                this.board[randrow][randcol] = OBSTACLE_CHAR;
                remain = remain - 1;
            }
        }
    }

    /**
     * add count number of zapper to the board with random direction of
     * zappers, and random position.
     *
     * @param count the number of zappers we want to add into the board.
     */
    void addRandomZappers(int count) {
        // check if the zappers  exceed the available spaces
        int num = countEmptyTiles();
        if (count > num || count < 0) {
            return;
        }
        Random r = new Random();
        int remain = count;
        // randomly add zappers
        while (remain > 0) {
            int randrow = r.nextInt(this.board.length);
            int randcol = r.nextInt(this.board[0].length);
            if (this.board[randrow][randcol] == SPACE_CHAR) {
                int direction = r.nextInt(RANDOMNUMFOUR);
                // depend on the random number, zero, one, two, three
                // we choose the direction of the zapper
                switch (direction) {
                    case 0:
                        this.board[randrow][randcol] = DOWN_ZAP_CHAR;
                        break;
                    case 1:
                        this.board[randrow][randcol] = UP_ZAP_CHAR;
                        break;
                    case RANDOMCASETWO:
                        this.board[randrow][randcol] = LEFT_ZAP_CHAR;
                        break;
                    case RANDOMCASETHREE:
                        this.board[randrow][randcol] = RIGHT_ZAP_CHAR;
                        break;
                }
                remain = remain - 1;
            }
        }

    }

    /**
     * a helper method, but we abandon it here since we don't use it and it's
     * not graded.
     *
     * @param zapChar not used
     * @return -1, nut used.
     */
    int indexOfZapper(char zapChar) {
        // TODO
        return -1;
    }

    /**
     * rotate the board counterclockwise once, and all the instance variables
     * of the GameStates are also changed.
     */
    void rotateCounterClockwise() {
        //create new char board
        char[][] rotated = new char[board[0].length][board.length];
// rotate counterclockwise
        for (int i = 0; i < board[0].length; ++i) {
            for (int j = 0; j < board.length; ++j) {

                char prev = board[j][board[0].length - i - 1];

                rotated[i][j] = prev;
                //for goal
                if (prev == GOAL_CHAR) {
                    this.goalRow = i;
                    this.goalCol = j;
                }
                //for player
                if (prev == PLAYER_CHAR) {
                    this.playerRow = i;
                    this.playerCol = j;
                }
            }

        }
        // rotate the directions of zappers
        for (int x = 0; x < rotated.length; ++x) {
            for (int y = 0; y < rotated[0].length; ++y) {
                if (rotated[x][y] == UP_ZAP_CHAR) {
                    rotated[x][y] = LEFT_ZAP_CHAR;
                } else if (rotated[x][y] == DOWN_ZAP_CHAR) {
                    rotated[x][y] = RIGHT_ZAP_CHAR;
                } else if (rotated[x][y] == RIGHT_ZAP_CHAR) {
                    rotated[x][y] = UP_ZAP_CHAR;
                } else if (rotated[x][y] == LEFT_ZAP_CHAR) {
                    rotated[x][y] = DOWN_ZAP_CHAR;
                }
            }
        }
        this.board = rotated;


    }

    /**
     * Move the snake to left until it stop. The snake will leave a trail. If
     * the player reach the goal, we set levelPassed to true, and return.
     */
    void moveLeft() {
        // if it can move , move left
        while (playerCol > 0
                && board[playerRow][playerCol - 1] == SPACE_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
        }
        //encounter left zapper, move left
        if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == LEFT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveLeft();
        }
        //encounter right zapper
        else if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == RIGHT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveright();
        }
        // encounter up zapper
        else if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == UP_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveup();
        }
        // encounter down zapper
        else if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == DOWN_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            movedown();
        }
        // encounter obstacle don't move
        else if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == OBSTACLE_CHAR) {
            return;
        }
        //encounter goal, terminate game
        else if (playerCol - 1 >= 0
                && board[playerRow][playerCol - 1] == GOAL_CHAR) {
            this.levelPassed = true;
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            return;
        }
    }

    /**
     * move in any direction. We need to rotate the game board several times,
     * and then move the snake left, and then rotate teh game board back.
     *
     * @param direction the direction that we want to the snake to move.
     */
    void move(Direction direction) {
        if (direction == Direction.LEFT) {
            moveLeft();//call helper method
        } else if (direction == Direction.RIGHT) {
            moveright();//call helper method
        } else if (direction == Direction.UP) {
            moveup();//call helper method
        } else if (direction == Direction.DOWN) {
            movedown();//call helper method
        }
    }

    /**
     * override the toString method for the GameState object. Return a string
     * representation of the GameState object.
     *
     * @return a string representation of the GameState object.
     */
    @Override
    public String toString() {
        int cols = this.board[0].length;
        int rows = this.board.length;
        int num_dash = 2 * cols + 3;
        StringBuilder result = new StringBuilder();
        //fill the top wall
        for (int a = 0; a < num_dash; a++) {
            result.append(HORIZONTAL_BORDER_CHAR);
        }
        result.append(NEWLINE_CHAR);
        //fill the rows
        int i = 0;
        while (i < rows) {
            result.append(SIDE_BORDER_CHAR);
            result.append(SPACE_CHAR);
            for (int j = 0; j < cols; j++) {
                result.append(this.board[i][j]);
                result.append(SPACE_CHAR);
            }
            result.append(SIDE_BORDER_CHAR);
            result.append(NEWLINE_CHAR);
            i = i + 1;
        }

        //fill the bottom
        for (int b = 0; b < num_dash; b++) {
            result.append(HORIZONTAL_BORDER_CHAR);
        }
        result.append(NEWLINE_CHAR);
        String final_result = result.toString();
        return final_result;
    }

    /**
     * deep comparison, compare every field in the calling object to the
     * other object.
     *
     * @param other The object we compare to.
     * @return if all fields of teh two object we compare are teh same,
     * return true, otherwise, return false. Return false if teh parameter is
     * null.
     */
    @Override
    public boolean equals(Object other) {
        //check if it is null
        if (other == null) {
            return false;
        }
        //check if it is GameState class
        if (!(other instanceof GameState)) {
            return false;
        }
        //cast object
        GameState otherS = (GameState) other;
        //check all instance variables
        if (this.board[0].length != otherS.board[0].length) {
            return false;
        }
        if (this.board.length != otherS.board.length) {
            return false;
        }
        //check the goal position
        if (this.goalRow != otherS.goalRow ||
                this.goalCol != otherS.goalCol) {
            return false;
        }
        //check player row
        if (this.playerRow != otherS.playerRow ||
                this.playerCol != otherS.playerCol) {
            return false;
        }
        //check levelPassed
        if (this.levelPassed != otherS.levelPassed) {
            return false;
        }

        //Check the string representation if is the same.
        if (this.toString().equals(otherS.toString())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * private Helper method to move teh player upward.
     * Used for debug
     */
    private void moveup() {
        while (playerRow > 0
                && board[playerRow - 1][playerCol] == SPACE_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == LEFT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveLeft();
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == RIGHT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveright();
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == DOWN_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            movedown();
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == UP_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveup();
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == OBSTACLE_CHAR) {
            return;
        }
        if (playerRow - 1 >= 0
                && board[playerRow - 1][playerCol] == GOAL_CHAR) {
            this.levelPassed = true;
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow - 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            return;
        }
    }

    /**
     * private Helper method to move teh player downward.
     * Used for debug
     */
    private void movedown() {
        while (playerRow < board.length - 1
                && board[playerRow + 1][playerCol] == SPACE_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == LEFT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveLeft();
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == RIGHT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveright();
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == DOWN_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            movedown();
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == UP_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveup();
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == OBSTACLE_CHAR) {
            return;
        }
        if (playerRow + 1 <= board.length - 1
                && board[playerRow + 1][playerCol] == GOAL_CHAR) {
            this.levelPassed = true;
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerRow = playerRow + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            return;
        }

    }

    /**
     * private Helper method to move teh player right.
     * Used for debug
     */
    private void moveright() {
        while (playerCol < board[0].length - 1
                && board[playerRow][playerCol + 1] == SPACE_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
        }
        if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == LEFT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveLeft();
        } else if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == RIGHT_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveright();
        } else if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == UP_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            moveup();
        } else if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == DOWN_ZAP_CHAR) {
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            movedown();
        } else if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == OBSTACLE_CHAR) {
            return;
        } else if (playerCol + 1 <= board[0].length - 1
                && board[playerRow][playerCol + 1] == GOAL_CHAR) {
            this.levelPassed = true;
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            playerCol = playerCol + 1;
            board[playerRow][playerCol] = PLAYER_CHAR;
            return;
        }
    }


}
