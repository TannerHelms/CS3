import java.util.Scanner;
import java.util.*;

public class Game {
    private static final String SOLVED_ID = "123456780";
    Board theBoard;
    String originalBoardID;
    String boardName;

    /**
     *  Solve the provided board
     * @param label Name of board (for printing)
     * @param b Board to be solved
     */
    public void playGiven(String label, Board b) {
        theBoard = b;
        originalBoardID = b.getId();
        boardName = label;
        System.out.println("Board initial: " + boardName + " \n" + theBoard.toString());
        if (!isBoardSolvable(b))
        {
            System.err.println(boardName + " is not solvable");
            return;
        }
        solve();
    }

    /**
     *
     * Validate that the gived board is solvable
     *
     * @param b the game board that we are checking
     * @return if the the given board is solvable or not
     */
    private boolean isBoardSolvable(Board b) {
        ArrayList<Integer> boardValues = new ArrayList<Integer>(){};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.GetBoard()[i][j] == 0)
                {
                    continue;
                }
                boardValues.add(b.GetBoard()[i][j]);
            }
        }

        int inversionCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i < j && boardValues.get(i) > boardValues.get(j))
                {
                    inversionCount++;
                }
            }
        }
        if (inversionCount % 2 != 0)
        {
            return false;
        }
        return true;
    }

    /**
     * Takes the game board and attempts to solve it
     * @return if the function was able to solve the board or not
     */
    private boolean solve() {
        // starting time
        long start = System.currentTimeMillis();
        // Generate a new Queue
        Queue<NumberBoard> queue = new Queue<>();

        // Enable DebugMode
        //queue.DebugMode();

        // Add the first board to the queue
        queue.Add(new NumberBoard(this.theBoard, ""));
        // Create a boolean to check if the board is solved
        boolean solved = false;
        NumberBoard solvedBoard = null;
        while (!solved) {
            //make sure that we have at least 1 thing on the queue
            if (queue.GetSize() < 1)
            {
                return false;
            }

            // Take the first item off the queue
            NumberBoard numberBoard = queue.remove();
            var up = new Board(numberBoard.board);
            if (up.slideUp())
            {
                if (up.isSolved(up.toString()))
                {
                    solved = true;
                    solvedBoard = new NumberBoard(up, numberBoard.steps + "U");
                    continue;
                }
                queue.Add(new NumberBoard(up, numberBoard.steps + "U"));
            }

            // Check to see if a down move is valid
            var down = new Board(numberBoard.board);
            if (down.slideDown())
            {
                if (down.isSolved(down.toString()))
                {
                    solvedBoard = new NumberBoard(down, numberBoard.steps + "D");
                    solved = true;
                    continue;
                }
                queue.Add(new NumberBoard(down, numberBoard.steps + "D"));
            }

            // Check to see if a left move is valid
            var left = new Board(numberBoard.board);
            if (left.slideLeft())
            {
                if (left.isSolved(left.toString()))
                {
                    solvedBoard = new NumberBoard(left, numberBoard.steps + "L");
                    solved = true;
                    continue;
                }
                queue.Add(new NumberBoard(left, numberBoard.steps + "L"));
            }

            // Check to see if a right move is valid
            var right = new Board(numberBoard.board);
            if (right.slideRight())
            {
                if (right.isSolved(right.toString()))
                {
                    solvedBoard = new NumberBoard(right, numberBoard.steps + "R");
                    solved = true;
                    continue;
                }
                queue.Add(new NumberBoard(right, numberBoard.steps + "R"));
            }
            //queue.printContents();
        }

        printSteps(this, solvedBoard.steps, queue.added, queue.removed);
        // ending time
        long end = System.currentTimeMillis();
        System.out.println("This Puzzle took " +
                (end - start) + "ms to solve");
        return true;
    }

    // Print steps of a solved board
    private void printSteps(Game g, String moves, int added, int removed) {
        System.out.println("Solution");
        System.out.println(g.theBoard.toString());
        var move = moves.split("");
        for (var m : move) {
            System.out.println(m + "==>");
            switch (m){
                case "U":
                    g.theBoard.slideUp();
                    System.out.println(g.theBoard.toString());
                    break;
                case "D":
                    g.theBoard.slideDown();
                    System.out.println(g.theBoard.toString());
                    break;
                case "L":
                    g.theBoard.slideLeft();
                    System.out.println(g.theBoard.toString());
                    break;
                case "R":
                    g.theBoard.slideRight();
                    System.out.println(g.theBoard.toString());
            }
        }
        System.out.printf("Moves Required: %s(%d)\n", moves, moves.length());
        System.out.println(g.boardName + " Queue Added=" + added + " Removed=" + removed);

    }

    /**
     * Create a holder for the board and the steps that were
     * used to solve it
     */
    private class NumberBoard {
        Board board;
        String steps = "";
        public NumberBoard(Board board){
            this.board = board;
        }
        public NumberBoard(Board board, String steps) {
            this.board = board;
            this.steps = this.steps + steps;
        }


        @Override
        public String toString()
        {
            return "State: "+ board.toString().replace(" ", "").replace("\n", "") + " Steps: "+steps;
        }


    }

    /**
     * Create a random board (which is solvable) by jumbling jumnbleCount times.
     * Solve
     * @param label Name of board (for printing)
     * @param jumbleCount number of random moves to make in creating a board
     */
    public void playRandom(String label, int jumbleCount) {
        theBoard = new Board();
        theBoard.makeBoard(jumbleCount);
        System.out.println(label + "\n" + theBoard);
        playGiven(label, theBoard);
    }



    public static void main(String[] args) {

        String[] games = {"102453786", "123740658", "023156478", "413728065", "145236078", "123456870"};
        String[] gameNames = {"Easy Board", "Game1", "Game2", "Game3", "Game4", "Game5 No Solution"};
        Game g = new Game();
        Scanner in = new Scanner(System.in);
        Board b;
        String resp;
        for (int i = 0; i < games.length; i++) {
            b = new Board(games[i]);
            g.playGiven(gameNames[i], b);
            System.out.println("Click any key to continue");
            resp = in.nextLine();
        }


        boolean playAgain = true;
        //playAgain = false;

        int JUMBLECT = 18;  // how much jumbling to do in random board
        while (playAgain) {
            g.playRandom("Random Board", JUMBLECT);

            System.out.println("Play Again?  Answer Y for yes\n");
            resp = in.nextLine().toUpperCase();
            playAgain = (resp != "") && (resp.charAt(0) == 'Y');
        }


    }


}
