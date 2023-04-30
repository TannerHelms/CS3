import java.util.Scanner;
import java.util.*;

public class Game {
    private static final String SOLVED_ID = "123456780";
    Board theBoard;
    String originalBoardID;
    String boardName;

    /**
     * Solve the provided board
     *
     * @param label Name of board (for printing)
     * @param b     Board to be solved
     */
    public void playGiven(String label, Board b) {
        theBoard = b;
        originalBoardID = b.getId();
        boardName = label;
        System.out.println("Board initial: " + boardName + " \n" + theBoard.toString());
        if (!isBoardSolvable(b)) {
            System.err.println(boardName + " is not solvable");
            return;
        }
        String priority = solve();
        System.out.println();
        String bruteForce = solveBruteForce();
        System.out.println();
        if (priority.equals(bruteForce)){
            System.out.println("A* and Brute have the same solution");
        } else {
            System.out.println("A* and Brute do not have the same solution");
        }
    }

    /**
     * Validate that the gived board is solvable
     *
     * @param b the game board that we are checking
     * @return if the the given board is solvable or not
     */
    private boolean isBoardSolvable(Board b) {
        ArrayList<Integer> boardValues = new ArrayList<Integer>() {
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.GetBoard()[i][j] == 0) {
                    continue;
                }
                boardValues.add(b.GetBoard()[i][j]);
            }
        }

        int inversionCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i < j && boardValues.get(i) > boardValues.get(j)) {
                    inversionCount++;
                }
            }
        }
        if (inversionCount % 2 != 0) {
            return false;
        }
        return true;
    }

    /**
     * Takes the game board and attempts to solve it
     *
     * @return if the function was able to solve the board or not
     */


    private String solve() {
        // starting time
        long start = System.currentTimeMillis();
        // Generates a new Priority Queue
        AVLTree<Board> queue = new AVLTree<>();
        // Generate a new Brute Force Queue
        //Queue<Board> queue = new Queue<>();
        // Enable DebugMode
        //queue.DebugMode();
        // Add the first board to the queue
        queue.insert(new Board(this.theBoard));
        // Create a boolean to check if the board is solved
        boolean solved = false;
        Board solvedBoard = null;
        while (!solved) {
            //make sure that we have at least 1 thing on the queue
            if (queue.isEmpty()) {
                return "";
            }

            // Take the first item off the queue
            Board b = queue.deleteMin();
            var up = new Board(b);
            if (up.slideUp()) {
                if (up.isSolved(up.toString())) {
                    solved = true;
                    solvedBoard = new Board(up, b.GetSteps() + "U");
                    continue;
                }
                queue.insert(new Board(up, b.GetSteps() + "U"));
            }

            // Check to see if a down move is valid
            var down = new Board(b);
            if (down.slideDown()) {
                if (down.isSolved(down.toString())) {
                    solvedBoard = new Board(down, b.GetSteps() + "D");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(down, b.GetSteps() + "D"));
            }

            // Check to see if a left move is valid
            var left = new Board(b);
            if (left.slideLeft()) {
                if (left.isSolved(left.toString())) {
                    solvedBoard = new Board(left, b.GetSteps() + "L");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(left, b.GetSteps() + "L"));
            }

            // Check to see if a right move is valid
            var right = new Board(b);
            if (right.slideRight()) {
                if (right.isSolved(right.toString())) {
                    solvedBoard = new Board(right, b.GetSteps() + "R");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(right, b.GetSteps() + "R"));
            }
            //queue.printContents();
        }
        System.out.println("A* Solve");
        printSteps(this, solvedBoard.GetSteps(), queue.added, queue.removed);
        // ending time
        long end = System.currentTimeMillis();
        System.out.println("Solve Time: " + (end - start)+"ms");
        return solvedBoard.GetSteps();
    }
    private String solveBruteForce() {

        // starting time
        long start = System.currentTimeMillis();
        // Generates a new Priority Queue
        BruteForce<Board> queue = new BruteForce<>();
        // Generate a new Brute Force Queue
        //Queue<Board> queue = new Queue<>();
        // Enable DebugMode
        //queue.DebugMode();
        // Add the first board to the queue
        queue.insert(new Board(this.theBoard));
        // Create a boolean to check if the board is solved
        boolean solved = false;
        Board solvedBoard = null;
        while (!solved) {
            //make sure that we have at least 1 thing on the queue
            if (queue.isEmpty()) {
                return "";
            }

            // Take the first item off the queue
            Board b = queue.deleteMin();
            var up = new Board(b);
            if (up.slideUp()) {
                if (up.isSolved(up.toString())) {
                    solved = true;
                    solvedBoard = new Board(up, b.GetSteps() + "U");
                    continue;
                }
                queue.insert(new Board(up, b.GetSteps() + "U"));
            }

            // Check to see if a down move is valid
            var down = new Board(b);
            if (down.slideDown()) {
                if (down.isSolved(down.toString())) {
                    solvedBoard = new Board(down, b.GetSteps() + "D");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(down, b.GetSteps() + "D"));
            }

            // Check to see if a left move is valid
            var left = new Board(b);
            if (left.slideLeft()) {
                if (left.isSolved(left.toString())) {
                    solvedBoard = new Board(left, b.GetSteps() + "L");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(left, b.GetSteps() + "L"));
            }

            // Check to see if a right move is valid
            var right = new Board(b);
            if (right.slideRight()) {
                if (right.isSolved(right.toString())) {
                    solvedBoard = new Board(right, b.GetSteps() + "R");
                    solved = true;
                    continue;
                }
                queue.insert(new Board(right, b.GetSteps() + "R"));
            }
            //queue.printContents();
        }

        System.out.println("Brute Force");
        printSteps(this, solvedBoard.GetSteps(), queue.added, queue.removed);
        // ending time
        long end = System.currentTimeMillis();
        System.out.println("Solve Time: " + (end - start)+"ms");
        return solvedBoard.GetSteps();
    }

    // Print steps of a solved board
    private void printSteps(Game g, String moves, int added, int removed) {
        /*
        System.out.println("Solution");
        System.out.println(g.theBoard.toString());
        var move = moves.split("");
        for (var m : move) {
            System.out.println(m + "==>");
            switch (m) {
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
         */
        System.out.printf("Moves Required: %s(%d)\n", moves, moves.length());
        System.out.println("Enqueued=" + added + " Dequeued=" + removed);

    }
    /**
     * Create a random board (which is solvable) by jumbling jumnbleCount times.
     * Solve
     *
     * @param label       Name of board (for printing)
     * @param jumbleCount number of random moves to make in creating a board
     */
    public void playRandom(String label, int jumbleCount) {
        theBoard = new Board();
        theBoard.makeBoard(jumbleCount);
        System.out.println(label + "\n" + theBoard);
        playGiven(label, theBoard);
    }
}


