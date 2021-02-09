import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] tiles;
    private int zeroIndex;
    // private boolean isGoalBoard;
    
    // cache fixed values to avoid redundant and repetitive computation
    private int[][] goalBoardTiles;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;
    private String boardStringRepr = "";

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // check input
        if (tiles == null) {
            throw new IllegalArgumentException("null tiles");
        } else if (tiles.length == 0) {
            throw new IllegalArgumentException("empty tiles");
        } else if (tiles[0] == null || tiles[0].length == 0) {
            throw new IllegalArgumentException("array in tiles is null or empty");
        }

        // for goal board constructor

        // init instance variable
        this.tiles = new int[tiles.length][tiles.length];
        int dim = tiles.length;
        this.goalBoardTiles = new int[tiles.length][tiles.length];
        // deep copy
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int valueToCopy = tiles[i][j];
                this.tiles[i][j] = valueToCopy;
                goalBoardTiles[i][j] = expectedValue(i, j); 
                // goalBoardTiles[i][j] = i * dim + (j + 1);
                if (valueToCopy == 0) {
                    this.zeroIndex = i * dim + j;
                }
            }
        }

        // Board compareBoard = this.goalBoard();
        // this.isGoalBoard= equals(compareBoard);
        this.hammingDistance = hamming();
        this.manhattanDistance = manhattan();
    }

    // string representation of this board
    public String toString() {
        if (this.boardStringRepr.length() == 0) {
            int dim = this.tiles.length;

            StringBuilder s = new StringBuilder();
            s.append(dim + "\n");

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    s.append(String.format("%2d ", this.tiles[i][j]));
                }
                s.append("\n");
            }

            this.boardStringRepr = s.toString();
        }

        return this.boardStringRepr;
    }

    /**
     * @return the dimension of the board
     */
    public int dimension() {
        int dim = this.tiles.length;
        return dim;
    }

    /**
     * @return the hamming value, or the tiles out of place (in terms of the goal
     *         board) of the current board
     */
    public int hamming() {
        if (this.hammingDistance != -1) {
            int hamming = this.hammingDistance;
            return hamming;
        }

        int dim = dimension();
        int goalElement = 1;
        int hammingValue = 0;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.tiles[i][j] != goalElement) {
                    if (i != dim - 1 || j != dim - 1)
                        hammingValue++;
                }
                goalElement++;
            }
        }

        return hammingValue;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (this.manhattanDistance!= -1) {
            int manhattan = this.manhattanDistance;
            return manhattan;
        }

        // init
        int totalDistance = 0;
        int expectedValue;
        int dim = dimension();

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int currentValue = this.tiles[i][j];
                expectedValue = expectedValue(i, j);
                if (expectedValue != this.tiles[i][j]) {
                    int expectedRow = expectedRow(currentValue);
                    int expectedColumn = expectedColumn(currentValue);
                    int currentManhattan = Math.abs(expectedRow - i) + Math.abs(expectedColumn - j);
                    totalDistance += currentManhattan;
                }
                expectedValue++;
            }
        }

        return totalDistance;
    }

    /**
     * The expected value on a goal board at certain index.
     * 
     * row and col and 0 indexed.
     **/
    private int expectedValue(int row, int col) {
        int dim = dimension();

        if (row == dim - 1 && col == dim - 1)
            return 0;

        return row * dim + (col + 1);
    }

    /**
     * helper function for manhattan to calculate supposed row position 0 indexed.
     **/
    private int expectedRow(int value) {
        int dim = dimension();
        if (value == 0)
            return dim - 1;
        return (value - 1) / dim;
    }

    /**
     * helper function for manhattan to calculate supposed column position 0
     * indexed.
     **/
    private int expectedColumn(int value) {
        int dim = dimension();
        if (value == 0)
            return dim - 1;
        return (value - 1) % dim;
    }

    private Board goalBoard() {
        Board goalBoard = new Board(this.goalBoardTiles);
        return goalBoard;
        // int dim = dimension();
        // int[][] goalBoardTiles = new int[dim][dim];
        // int goalElement = 1;

        // for (int i = 0; i < dim; i++) {
            // for (int j = 0; j < dim; j++) {
                // if (i == dim - 1 && j == dim - 1) {
                    // goalBoardTiles[i][j] = 0;
                // } else {
                    // goalBoardTiles[i][j] = goalElement;
                    // goalElement++;
                // }
            // }
        // }

        // Board goalBoard = new Board(goalBoardTiles);
        // return goalBoard;
    }

    // is this board the goal board?
    public boolean isGoal() {
        Board target = goalBoard();
        boolean isGoalBoard = this.equals(target);
        return isGoalBoard;

        // int dim = dimension();
        // int goalElement = 1;

        // for (int i = 0; i < dim; i++) {
        // for (int j = 0; j < dim; j++) {
        // if (this.tiles[i][j] != goalElement) {
        // if (i != dim - 1 || j != dim - 1) return false;
        // }
        // goalElement++;
        // }
        // }

        // return true;
    }

    /** 
     * does this board equal y?
     * A hack is used.
     */
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board yBoard = (Board) y;
        if (yBoard.dimension() != dimension())
            return false;

        String s = toString();
        String ys = yBoard.toString();
        if (s.equals(ys))
            return true;

        return false;
    }

    /**
     * all neighboring boards (next possible state)
     **/
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighboringBoards = new ArrayList<>();

        int dim = dimension();

        // edge case: no neighboring board
        if (dim == 0 || dim == 1)
            return null;

        // convert zeroIndex to row, col
        int zeroRowIndex = this.zeroIndex / dim;
        int zeroColIndex = this.zeroIndex % dim;

        //
        int[][] neighboringTiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            neighboringTiles[i] = Arrays.copyOf(this.tiles[i], dim);
        }

        if (zeroRowIndex < dim - 1) {
            // swap
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex, zeroRowIndex + 1, zeroColIndex);

            // construct board
            Board neighboringBoard = new Board(neighboringTiles);
            neighboringBoards.add(neighboringBoard);

            // swap back
            swapElements(neighboringTiles, zeroRowIndex + 1, zeroColIndex, zeroRowIndex, zeroColIndex);
        }

        if (zeroRowIndex > 0) {
            // swap
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex, zeroRowIndex - 1, zeroColIndex);

            // construct board
            Board neighboringBoard = new Board(neighboringTiles);
            neighboringBoards.add(neighboringBoard);

            // swap back
            swapElements(neighboringTiles, zeroRowIndex - 1, zeroColIndex, zeroRowIndex, zeroColIndex);
        }

        if (zeroColIndex < dim - 1) {
            // swap
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex + 1, zeroRowIndex, zeroColIndex);

            // construct board
            Board neighboringBoard = new Board(neighboringTiles);
            neighboringBoards.add(neighboringBoard);

            // swap back
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex, zeroRowIndex, zeroColIndex + 1);
        }

        if (zeroColIndex > 0) {
            // swap
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex - 1, zeroRowIndex, zeroColIndex);

            // construct board
            Board neighboringBoard = new Board(neighboringTiles);
            neighboringBoards.add(neighboringBoard);

            // swap back
            swapElements(neighboringTiles, zeroRowIndex, zeroColIndex, zeroRowIndex, zeroColIndex - 1);
        }

        return neighboringBoards;
    }

    /**
     * Swap helper function for computing neighboring and twinboards.
     **/
    private void swapElements(int[][] tiles, int rowA, int colA, int rowB, int colB) {
        int temp = tiles[rowA][colA];
        tiles[rowA][colA] = tiles[rowB][colB];
        tiles[rowB][colB] = temp;
    }

    /**
     * Randomly swap two elements that are non-zero to get a twin board.
     **/
    public Board twin() {
        int dim = dimension();

        // edge case: no twin board
        if (dim == 0 || dim == 1)
            return null;

        int elementOneIndex = 0, elementTwoIndex = 0;

        int[][] twinBoardTiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            twinBoardTiles[i] = Arrays.copyOf(this.tiles[i], dim);
        }

        // select two elements uniformly and make sure they are i. non-zero ii. not
        // equal
        while (elementOneIndex == zeroIndex || elementTwoIndex == zeroIndex || elementOneIndex == elementTwoIndex) {
            elementOneIndex = StdRandom.uniform(0, dim * dim);
            elementTwoIndex = StdRandom.uniform(0, dim * dim);
        }

        swapElements(twinBoardTiles, elementOneIndex / dim, elementOneIndex % dim, elementTwoIndex / dim,
                elementTwoIndex % dim);

        Board twinBoard = new Board(twinBoardTiles);

        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] t0 = null;
        int[][] t00 = { {} };
        int[][] t1 = { { 0 } };
        int[][] t2 = { { 1, 2 }, { 3, 0 } };
        int[][] t3 = { { 1, 2 }, { 3, 0 } };
        int[][] t4 = { { 1, 3 }, { 2, 0 } };

        Board aBoard;
        Board bBoard;
        String aBoardString;
        String bBoardString;
        String cmpString;

        // null test
        {
            try {
                Board b0 = new Board(t0);
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                }
            }
        }

        // empty array test
        {
            try {
                Board b00 = new Board(t00);
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                }
            }
        }

        // test equality
        {
            aBoard = new Board(t1);
            aBoardString = aBoard.toString();
            cmpString = "1\n 0 \n";
            assert cmpString.equals(aBoardString);
            if (!cmpString.equals(aBoardString))
                System.out.println(aBoardString);

            aBoard = new Board(t2);
            aBoardString = aBoard.toString();
            cmpString = "2\n 1  2 \n 3  0 \n";
            assert cmpString.equals(aBoardString);
            if (!cmpString.equals(aBoardString))
                System.out.println(aBoardString);

            bBoard = new Board(t3);
            bBoardString = bBoard.toString();
            assert cmpString.equals(bBoardString);
            if (!cmpString.equals(bBoardString))
                System.out.println(bBoardString);

            assert aBoard.equals(bBoard);
        }

        // test inequality
        {
            bBoard = new Board(t4);
            bBoardString = bBoard.toString();
            cmpString = "2\n 1  3 \n 2  0 \n";
            if (!cmpString.equals(bBoardString))
                System.out.println(bBoardString);
            assert (!aBoard.equals(bBoard));
        }

        // test goal board
        {
            assert aBoard.isGoal();
            assert !bBoard.isGoal();
        }

        // test hamming value
        {
            assert aBoard.hamming() == 0;
            assert bBoard.hamming() == 2;
        }

        // test manhattan
        {
            assert aBoard.manhattan() == 0;
            assert bBoard.manhattan() == 4;
        }

        // test neighboring boards
        {
            int[][] cmp1 = { { 1, 0 }, { 3, 2 } };
            int[][] cmp2 = { { 1, 2 }, { 0, 3 } };
            ArrayList<Board> cmpBoards = new ArrayList<>();
            cmpBoards.add(new Board(cmp1));
            cmpBoards.add(new Board(cmp2));
            int count = 0;
            for (Board a : aBoard.neighbors()) {
                for (Board b : cmpBoards) {
                    if (a.equals(b))
                        count++;
                }
            }
            assert count == 2;
            int[][] t5 = { { 1, 2, 3 }, { 4, 0, 5 }, { 6, 7, 8 } };
            aBoard = new Board(t5);
            int[][] t5Cmp1 = { { 1, 0, 3 }, { 4, 2, 5 }, { 6, 7, 8 } };
            int[][] t5Cmp2 = { { 1, 2, 3 }, { 0, 4, 5 }, { 6, 7, 8 } };
            int[][] t5Cmp3 = { { 1, 2, 3 }, { 4, 7, 5 }, { 6, 0, 8 } };
            int[][] t5Cmp4 = { { 1, 2, 3 }, { 4, 5, 0 }, { 6, 7, 8 } };
            ArrayList<Board> t5CmpBoards = new ArrayList<>();
            t5CmpBoards.add(new Board(t5Cmp1));
            t5CmpBoards.add(new Board(t5Cmp2));
            t5CmpBoards.add(new Board(t5Cmp3));
            t5CmpBoards.add(new Board(t5Cmp4));
            int t5CmpCount = 0;
            for (Board a : aBoard.neighbors()) {
                for (Board b : t5CmpBoards) {
                    if (a.equals(b))
                        t5CmpCount++;
                }
            }
            assert t5CmpCount == 4;
        }

        // test twin
        {
            // recycle t5 and aBoard
            int testIteration = 50;
            for (int t = 0; t < testIteration; t++) {
                Board twinBoard = aBoard.twin();
                assert !aBoard.equals(twinBoard);
            }
        }
    }
}