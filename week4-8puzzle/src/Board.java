import java.util.ArrayList;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;

public class Board {
    // cache fixed values to avoid redundant and repetitive computation
    private final char[] tiles;
    private final int dimension;
    private int zeroIndex;
    private int hammingDistance = 0;
    private int manhattanDistance = 0;
    private final boolean isGoalBoard;

    // String representation doesn't change
    private String boardStringRepr = null;

    // return consistent twin board for the same board instance
    private Board twinBoard = null;

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

        // this.tiles = new int[tiles.length][tiles.length];
        int dim = tiles.length;
        this.dimension = tiles.length;
        // init instance variable
        this.tiles = new char[dim * dim];

        // deep copy
        // init hamming distance and manhattan
        int expectedValue = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int currentValue = tiles[i][j];
                this.tiles[index(i, j)] = (char) currentValue;
                if (currentValue == 0) {
                    this.zeroIndex = i * dim + j;
                }

                if (currentValue != 0 && currentValue != expectedValue) {
                    // hamming
                    this.hammingDistance++;

                    // manhattan
                    int expectedRow = expectedRow(currentValue);
                    int expectedColumn = expectedColumn(currentValue);
                    int currentManhattan = Math.abs(expectedRow - i) + Math.abs(expectedColumn - j);
                    this.manhattanDistance += currentManhattan;
                }

                expectedValue++;
            }
        }

        // is this a goal board?
        isGoalBoard = this.hammingDistance == 0;
    }

    // string representation of this board
    public String toString() {
        if (this.boardStringRepr == null) {
            int dim = dimension();

            StringBuilder s = new StringBuilder();
            s.append(dim + "\n");

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    // int currentIndex = index(i, j);
                    s.append(String.format("%2d ", (int) this.tiles[index(i, j)]));
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
        int dim = this.dimension;
        return dim;
    }

    /**
     * 1-D index to row of 2-D index tuple (row, column). 
     */
    // private int row(int index) {
        // int dim = dimension();
        // return (index - 1) / dim;
    // }

    /**
     * 1-D index to column of 2-D index tuple (row, column). 
     */
    // private int column(int index) {
        // int dim = dimension();
        // return index % dim;
    // }

    /**
     * 2-D index tuple (row, column) to 1-D index.
     */
    private int index(int row, int col) {
        int dim = dimension();
        return row * dim + col;
    }

    /**
     * @return the hamming value, or the tiles out of place (in terms of the goal
     *         board) of the current board
     */
    public int hamming() {
        return this.hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattanDistance;
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


    // is this board the goal board?
    public boolean isGoal() {
        return this.isGoalBoard;
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

        int[][] neighboringTiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                neighboringTiles[i][j] = (int) this.tiles[index(i, j)];
            }
        }
        // for (int i = 0; i < dim; i++) {
            // neighboringTiles[i] = Arrays.copyOf(this.tiles[i], dim);
        // }

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
     * 
     * It seems the grader expects the twin board to be consistent throughout
     * the life time of a same Board instance?
     **/
    public Board twin() {
        int dim = dimension();

        // every board with dim > 1 must have twins
        // otherwise return null
        if (dim > 1 && this.twinBoard == null) {
            int elementOneIndex = 0, elementTwoIndex = 0;

            int[][] twinBoardTiles = new int[dim][dim];
            // for (int i = 0; i < dim; i++) {
                // twinBoardTiles[i] = Arrays.copyOf(this.tiles[i], dim);
            // }
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    twinBoardTiles[i][j] = (int) this.tiles[index(i, j)];
                }
            }

            // select two elements uniformly and make sure they are i. non-zero ii. not
            // equal
            while (elementOneIndex == zeroIndex || elementTwoIndex == zeroIndex || elementOneIndex == elementTwoIndex) {
                elementOneIndex = StdRandom.uniform(0, dim * dim);
                elementTwoIndex = StdRandom.uniform(0, dim * dim);
            }

            swapElements(twinBoardTiles, elementOneIndex / dim, elementOneIndex % dim, elementTwoIndex / dim,
                    elementTwoIndex % dim);

            this.twinBoard = new Board(twinBoardTiles);
        }

        return this.twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        boolean specificTest = true;
        String fileName;

        In in; 
        if (specificTest) {
            fileName = "puzzle01.txt";
            in = new In(fileName);
            // in = new In("puzzle2x2-unsolvable1.txt");
        } else {
            fileName = args[0];
            in = new In(fileName);
        }

        // create initial board from file
        int n = in.readInt();
        int[][] testTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                testTiles[i][j] = in.readInt();
        Board initial = new Board(testTiles);

        // test manhattan
        System.out.printf(
            "File: %s\nBoard:\n%sManhattan: %d\n",
            fileName, initial, initial.manhattan());

        // test twin
        System.out.printf(
            "Twin Board:\n%s", initial.twin());
        
        // test neighbors
        for (Board b : initial.neighbors()) {
            System.out.println(b);
        }
    }
}