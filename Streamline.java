/**
 * File header for Streamline.java
 * Description: help player to play the game using keyboard, and update teh
 * game after every move.
 * name: Yuanhao Zhu
 */

import java.util.*;
import java.io.*;

/**
 * Class header for the Streamline class
 * define how to control the game, and update the status after every input move
 */
public class Streamline {
    /* Provided constants */
    final static int DEFAULT_HEIGHT = 6;
    final static int DEFAULT_WIDTH = 5;
    final static String OUTFILE_NAME = "saved_streamline_game";

    /* Add your `final static` constants here */


    /* Instance variables, do not add any */
    GameState currentState;
    List<GameState> previousStates;

    /**
     * initialized the currentState with default height and width. Add 3
     * random obstacles and zappers, and put goal and payer at the default
     * position.
     */
    public Streamline() {
        //Initialized the GameState
        this.currentState = new GameState(DEFAULT_HEIGHT, DEFAULT_WIDTH,
                DEFAULT_HEIGHT - 1, 0, 0,
                DEFAULT_WIDTH - 1);
        //add 3 obstacles
        this.currentState.addRandomObstacles(3);
        //Add 3 random zappers.
        this.currentState.addRandomZappers(3);
        //Initialze previous States to empty array list.
        this.previousStates = new ArrayList<>();

    }

    /**
     * Given constructor from the starter code.
     *
     * @param filename a file we want to read
     */
    public Streamline(String filename) {
        try {
            loadFromFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.previousStates = new ArrayList<GameState>();
    }


    /**
     * a helper method to implement to constructor II.
     *
     * @param filename the file we want to read
     * @throws IOException exception..
     */
    protected void loadFromFile(String filename) throws IOException {
        /**
        //read the specified instance variables
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        String line1 = sc.nextLine();
        String line2 = sc.nextLine();
        String line3 = sc.nextLine();
        //read all the instance variables
        int height = Character.getNumericValue(line1.charAt(0));
        int width = Character.getNumericValue(line1.charAt(2));
        int prow = Character.getNumericValue(line2.charAt(0));
        int pcol = Character.getNumericValue(line2.charAt(2));
        int grow = Character.getNumericValue(line3.charAt(0));
        int gcol = Character.getNumericValue(line3.charAt(2));
        this.currentState
                = new GameState(height, width, prow, pcol, grow, gcol);
        int currow = 0;
        int count = 0;
        char[][] b = new char[height][width];
        // copy the boards as in the txt file
        while (count < height && sc.hasNextLine()) {
            String line = sc.nextLine();
            for (int i = 0; i < 5; i++) {
                b[currow][i] = line.charAt(i);

            }
            currow = currow + 1;
            count = count + 1;

        }
        this.currentState.board = b;
        if (prow == grow && pcol == gcol) {
            this.currentState.levelPassed = true;
        }
*/
        //read the specified instance variables
        Scanner sc = new Scanner(new File(filename));
        int height = sc.nextInt();
        int width = sc.nextInt();
        int pRow = sc.nextInt();
        int pCol = sc.nextInt();
        int gRow = sc.nextInt();
        int gCol = sc.nextInt();

        sc.nextLine();
        GameState OGame = new GameState(height, width, pRow, pCol, gRow, gCol);
        previousStates = new ArrayList<GameState>();
        currentState = OGame;

        if (currentState.playerRow == currentState.goalRow &&
                currentState.playerCol == currentState.goalCol) {
            currentState.levelPassed = true;
        }
        //set up a counter
        int counter = 0;

        while (sc.hasNext()) {
            String board = sc.nextLine();
            for (int i = 0; i < width; i++) {
                currentState.board[counter][i] = board.charAt(i);
            }
            counter++;
        }

    }

    /**
     * save the current state of teh game before it gets updated.
     * @param direction the direction the player going to move next.
     */
    void recordAndMove(Direction direction) {
        if (direction == null) {
            return;
        }
        // record the previous steps in an arraylist
        GameState copy = new GameState(this.currentState);
        this.previousStates.add(copy);
        this.currentState.move(direction);
        if (copy.equals(this.currentState)) {
            this.previousStates.remove(copy);
        }

    }

    /**
     * Undo the last step based on teh previous step
     */
    void undo() {
        //if teh previous step is empty, do nothing.
        if (this.previousStates.size() == 0) {
            return;
        }
        // go the the previous step
        else {
            int last = previousStates.size() - 1;
            this.currentState = this.previousStates.get(last);
            this.previousStates.remove(last);
        }
    }

    /**
     * take the keyboard input and connect to the game.
     */
    void play() {
        // run the game, don't stop unless quit or reach goal
        while (true) {
            System.out.print(this.currentState.toString());
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            input = input.toLowerCase();
            if (input.length() > 1) {
                System.out.println("Command must be one char long.");
                continue;
            }
            if (!((input.equals("w")) || (input.equals("a"))
                    || (input.equals("s")) || (input.equals("d"))
                    || (input.equals("u")) ||
                    (input.equals("o")) || (input.equals("q")))) {
                System.out.println(
                        "Possible commands:\n w - up\n a - left\n s - " +
                                "down\n d - right\n u - " +
                        "undo\n o - save to file\n q - quit level");
                continue;
            }
            // move up
            if (input.equals("w")) {
                recordAndMove(Direction.UP);
                if (this.currentState.levelPassed == true) {
                    System.out.print(currentState);
                    System.out.println("Level passed!");
                    break;
                }
            }
            //mobve left
            if (input.equals("a")) {
                recordAndMove(Direction.LEFT);
                if (this.currentState.levelPassed == true) {
                    System.out.print(currentState);
                    System.out.println("Level passed!");
                    break;
                }
            }
            //move down
            if (input.equals("s")) {
                recordAndMove(Direction.DOWN);
                if (this.currentState.levelPassed == true) {
                    System.out.print(currentState);
                    System.out.println("Level passed!");
                    break;
                }
            }
            //move right
            if (input.equals("d")) {
                recordAndMove(Direction.RIGHT);
                if (this.currentState.levelPassed == true) {
                    System.out.print(currentState);
                    System.out.println("Level passed!");
                    break;
                }
            }
            //undo
            if (input.equals("u")) {
                undo();
                continue;
            }
            //save the text
            if (input.equals("o")) {
                saveToFile();
            }
            //terminate the file loop
            if (input.equals("q")) {
                break;
            }
        }
    }

    /**
     * Write the Streamline game into a text file with certain format
     */
    void saveToFile() {
        /**


        File file = new File(OUTFILE_NAME + ".txt");
        //write the instance variables at the top three lines
        try {
            PrintWriter pw = new PrintWriter(file);
            int height = this.currentState.board.length;
            int width = this.currentState.board[0].length;

            int prow = this.currentState.playerRow;
            int pcol = this.currentState.playerCol;
            int grow = this.currentState.goalRow;
            int gcol = this.currentState.goalCol;
            pw.println(Integer.toString(height) + ' ' + width);
            pw.println(Integer.toString(prow) + ' ' + pcol);
            pw.println(Integer.toString(grow) + ' ' + gcol);
            // write the board on file char by char
            for (int i = 0; i < currentState.board.length; i++) {
                StringBuilder s = new StringBuilder();
                for (int j = 0; j < currentState.board[0].length; j++) {
                    s.append(this.currentState.board[i][j]);
                }
                String result = s.toString();
                pw.println(result);
            }
            pw.close();
            //if no file found
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         */
        try {
            PrintWriter saveFile = new PrintWriter(new File(OUTFILE_NAME));
            saveFile.write(currentState.board.length + " " +
                    currentState.board[0].length + "\n");
            saveFile.write(currentState.playerRow + " " +
                    currentState.playerCol +
                    "\n");
            saveFile.write(currentState.goalRow + " " +
                    currentState.goalCol + "\n");

            for (int i = 0; i < currentState.board.length; i++) {
                StringBuilder line = new StringBuilder();

                for (int j = 0; j < currentState.board[0].length; j++) {
                    line.append(currentState.board[i][j]);
                }
                saveFile.write(line.toString() + "\n");
            }
            saveFile.close();
            System.out.println("Saved current state to: saved_streamline_game");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
