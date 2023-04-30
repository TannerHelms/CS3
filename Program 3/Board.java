import java.util.Arrays;
import java.util.Random;

//functional handling of an instance of the board. Can be created using an id.
public class Board implements Comparable<Board>{
    private static final int SIZE = 3;
    private static final String SOLVED_ID = "123456780";
    private int[][] board;  // Values of board
    private int blankRow;   // Row location of blank
    private int blankCol;   // Column location of blank
    private String steps = "";
    private String previousMove = "";

    public String GetSteps(){
        return steps;
    }
    public int PredictSolveLength(){
        int solveLength = this.steps.length();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != i*3+j+1){
                    solveLength += 1;
                }
            }
        }
        return solveLength;
    }
    @Override
    public int compareTo(Board b) {
        int solveLength = b.PredictSolveLength();
        return Integer.compare(this.PredictSolveLength(), solveLength);
    }
    public int[][] GetBoard()
    {
        return this.board;
    }
    /**
     * Generate a new board
     */
    public Board(Board b, String steps)
    {
        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            this.board[i] = Arrays.copyOf(b.board[i], SIZE);
        }
        this.blankRow = b.blankRow;
        this.blankCol = b.blankCol;
        this.previousMove = b.previousMove;
        this.steps = steps;
    }
    public Board(Board b)
    {
        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            this.board[i] = Arrays.copyOf(b.board[i], SIZE);
        }
        this.blankRow = b.blankRow;
        this.blankCol = b.blankCol;
        this.previousMove = b.previousMove;
    }
    public Board() {
        board = new int[SIZE][SIZE];
    }



    /**
     * @param state String representation of the board
     * @return true if board state is the solution
     */
    Boolean isSolved(String state) {
        state=state.replace(" ", "").replace("\n", "");
        return state.equals(SOLVED_ID);
    }

    /**
     * Create board from string version
     *
     * @param id
     */
    Board(String id) {
        board = new int[SIZE][SIZE];
        int c = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (id.charAt(c) == '0') {
                    blankRow = i;
                    blankCol = j;
                }
                //assigns each item of the string to the board as an int.
                board[i][j] = Integer.parseInt(id.substring(c, ++c));
            }
    }

    @Override
    /**
     * Convert matrix version of  board to ID
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] i : board) {
            for (int j : i) {
                sb.append(j + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //Create a board by performing legal moves on a board
//jumbleCt indicates the number of moves to make
//if jumbleCt ==0, return the winning board

    /**
     * Create a solved board then make jumbleCt random moves
     *
     * @param jumbleCt number of random moves to make
     */
    public void makeBoard(int jumbleCt) {
        int val = 1;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = val++;
        blankRow = SIZE - 1;
        blankCol = SIZE - 1;
        board[blankRow][blankCol] = 0;
        jumble(jumbleCt);
    }

    /**
     * create a board from a given set of values
     *
     * @param values values of board in order by rows
     */
    void makeBoard(int[] values) {
        int c = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (values[c] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
                board[i][j] = values[c++];
            }
    }


    /**
     * Perform a slide up operation, if possible
     *
     * @return true if slide up was possible
     */
    boolean slideUp()  // If possible, slides a tile up into the blank position.  Returns success of operation.
    {
        if (this.previousMove.equals("D"))
        {
            return false;
        }
        this.previousMove = "U";
        if (blankRow == SIZE - 1) return false;
        if (blankRow == SIZE - 1) {
            board[blankRow][blankCol] = board[0][blankCol];
            board[0][blankCol] = 0;
            blankRow = 0;
        } else {

            board[blankRow][blankCol] = board[blankRow + 1][blankCol];
            board[blankRow + 1][blankCol] = 0;
            blankRow += 1;
        }
        return true;
    }

    /**
     * Perform a slide Down operation, if possible
     *
     * @return true if slideDown was performed
     */
    boolean slideDown()  // If possible, slides a tile down into the blank position.  Returns success of operation.
    {
        if (this.previousMove.equals("U"))
        {
            return false;
        }
        this.previousMove = "D";
        if (blankRow == 0) return false;
        if (blankRow == 0) {
            board[blankRow][blankCol] = board[SIZE - 1][blankCol];
            board[SIZE - 1][blankCol] = 0;
            blankRow = SIZE - 1;
        } else {
            board[blankRow][blankCol] = board[blankRow - 1][blankCol];
            board[blankRow - 1][blankCol] = 0;
            blankRow -= 1;
        }
        return true;
    }

    /**
     * Perform a slide Left, if possible
     *
     * @return true if slide Left was done
     */
    boolean slideLeft()  // If possible, slides a tile left into the blank position.  Returns success of operation.
    {
        if (this.previousMove.equals("R"))
        {
            return false;
        }
        this.previousMove = "L";
        if (blankCol == SIZE - 1) return false;
        if (blankCol == SIZE - 1) {
            board[blankRow][blankCol] = board[0][blankCol];
            board[0][blankCol] = 0;
            blankCol = 0;
        } else {
            board[blankRow][blankCol] = board[blankRow][blankCol + 1];
            board[blankRow][blankCol + 1] = 0;
            blankCol += 1;
        }
        return true;
    }

    /**
     * Perform a slide Right, if possible
     *
     * @return true if slide Righrt was performed
     */
    boolean slideRight()  // If possible, slides a tile right into the blank position.  Returns success of operation.
    {
        if (this.previousMove.equals("L"))
        {
            return false;
        }
        this.previousMove = "R";
        if (blankCol == 0) return false;
        if (blankCol == 0) {
            board[blankRow][blankCol] = board[blankRow][SIZE - 1];
            board[blankRow][SIZE - 1] = 0;
            blankCol = SIZE - 1;
        } else {
            board[blankRow][blankCol] = board[blankRow][blankCol - 1];
            board[blankRow][blankCol - 1] = 0;
            blankCol -= 1;
        }

        return true;
    }

    /**
     * Randomly apply ct moves to the board, making sure they are legal and don't undo the previous move
     *
     * @param ct
     */
    void jumble(int ct) {
        Random rand = new Random();
        String moveStr = "UDLR";  // Moves representing Up, Down, Left, Right
        char lastMove = ' ';
        char thisMove;
        for (int i = 0; i < ct; i++) {
            thisMove = moveStr.charAt(rand.nextInt(4));
            while (!makeMove(thisMove, lastMove)) {
                thisMove = moveStr.charAt(rand.nextInt(4));

            }
            lastMove = thisMove;
        }
    }

    /**
     * If move is legal (not  undoing previous move), make it
     * @param move     Move to attempt
     * @param lastmove Previously completed move
     * @return success of move
     */
    boolean makeMove(char move, char lastmove) {
        // System.out.println("makeMove " + move + lastmove + "\n");
        boolean moved = false;
        switch (move) {
            case 'R':
                if (lastmove != 'L') {
                    moved = slideRight();
                }
                break;
            case 'L':
                if (lastmove != 'R') {
                    moved = slideLeft();
                }
                break;
            case 'D':
                if (lastmove != 'U') {
                    moved = slideDown();
                }
                break;
            case 'U':
                if (lastmove != 'D') {
                    moved = slideUp();
                }
                break;
        }
        return moved;
    }

    /**
     * @return String version of board
     */
    public String getId() {
        String id = "";
        for (int i[] : board) {
            for (int j : i) {
                id += j;
            }
        }
        return id;
    }


}




