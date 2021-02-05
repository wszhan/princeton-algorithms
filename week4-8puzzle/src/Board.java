import java.util.Arrays;

public class Board {
    // private Board previousBoard;
    // private int moves;
    private int[][] tiles;
    // private boolean isGoalBoard;
    // private int hammingDistance;
    // private int manhattanDistance;
    // private String tilesString;

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

        // init instance variable
        this.tiles = new int[tiles.length][tiles[0].length];

        // deep copy
        for (int i = 0; i < tiles.length; i++) {
            int[] tilesToCopy = tiles[i];
            int length = tilesToCopy.length;
            this.tiles[i] = Arrays.copyOf(tilesToCopy, length);
        }

        // Board compareBoard = this.goalBoard();
        // this.isGoalBoard= equals(compareBoard);
        // this.hammingDistance = hamming();
        // this.manhattanDistance = manhattan();
    }
                                           
    // string representation of this board
    public String toString() {
        int dim = this.tiles.length;

        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", this.tiles[i][j]));
            }
            s.append("\n");
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() { 
        int dim = this.tiles.length;
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        int dim = dimension();
        int goalElement = 1;
        int hammingValue = 0;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.tiles[i][j] != goalElement) {
                    if (i != dim - 1 || j != dim - 1) hammingValue++;
                }
                goalElement++;
            }
        }

        return hammingValue;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // init
        int totalDistance = 0;
        int expectedValue;
        int dim = dimension();

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int currentValue = this.tiles[i][j];
                expectedValue = expectedValue(i, j);
                if (expectedValue != this.tiles[i][j]) {
                    // System.out.printf(
                        // "Testing :\n%s\ncurrent i, j - %d, %d\nthis vs. expected - [%d] vs. [%d]\n", 
                        // toString(),
                        // i, j,
                        // this.tiles[i][j], 
                        // expectedValue
                        // );
                    int expectedRow = expectedRow(currentValue);
                    int expectedColumn = expectedColumn(currentValue);
                    int currentManhattan = Math.abs(expectedRow - i) + Math.abs(expectedColumn - j);
                    // System.out.printf(
                        // "Testing :\n%s\ncurrent i, j - %d, %d\nthis vs. expected - [%d] vs. [%d]\nexpected row and col - [%d], [%d]\nmanhattan value: %d\n", 
                        // toString(),
                        // i, j,
                        // this.tiles[i][j], 
                        // expectedValue,
                        // expectedRow, expectedColumn,
                        // currentManhattan
                        // );
                    totalDistance += currentManhattan;
                }
                expectedValue++;
            }
        }

        return totalDistance;
    }

    // row and col and 0 indexed
    private int expectedValue(int row, int col) {
        int dim = dimension();

        if (row == dim - 1 && col == dim - 1) return 0;

        return row * dim + (col + 1);
    }

    // helper function for manhattan to calculate supposed row position
    // 0 indexed
    private int expectedRow(int value) {
        int dim = dimension();
        if (value == 0) return dim - 1;
        return (value - 1) / dim;
    }

    // helper function for manhattan to calculate supposed col position
    // 0 indexed
    private int expectedColumn(int value) {
        int dim = dimension();
        if (value == 0) return dim - 1;
        return (value - 1) % dim;
    }

    private Board goalBoard() {
        int dim = dimension();
        int[][] goalBoardTiles = new int[dim][dim];
        int goalElement = 1;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == dim - 1 && j == dim - 1) {
                    goalBoardTiles[i][j] = 0;
                } else {
                    goalBoardTiles[i][j] = goalElement;
                    goalElement++;
                }
            }
        }

        Board goalBoard = new Board(goalBoardTiles);
        return goalBoard;
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

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board yBoard = (Board) y;
        if (yBoard.dimension() != dimension()) return false;

        String s = toString();
        String ys = yBoard.toString();
        if (s.equals(ys)) return true;

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] t0 = null;
        int[][] t00 = {{}};
        int[][] t1 = {{0}};
        int[][] t2 = {{1, 2}, {3, 0}};
        int[][] t3 = {{1, 2}, {3, 0}};
        int[][] t4 = {{1, 3}, {2, 0}};

        Board aBoard;
        Board bBoard;
        String aBoardString;
        String bBoardString;
        String cmpString;

        // null test
        try {
            Board b0 = new Board(t0);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) { }
        }

        // empty array test
        try {
            Board b00 = new Board(t00);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) { }
        }

        aBoard = new Board(t1);
        aBoardString = aBoard.toString();
        cmpString = "1\n 0 \n";
        if (!cmpString.equals(aBoardString)) System.out.println(aBoardString);

        // test equality
        aBoard = new Board(t2);
        aBoardString = aBoard.toString();
        cmpString = "2\n 1  2 \n 3  0 \n";
        if (!cmpString.equals(aBoardString)) System.out.println(aBoardString);

        bBoard = new Board(t3);
        bBoardString = bBoard.toString();
        if (!cmpString.equals(bBoardString)) System.out.println(bBoardString);

        assert aBoard.equals(bBoard);

        // test inequality
        bBoard = new Board(t4);
        bBoardString = bBoard.toString();
        cmpString = "2\n 1  3 \n 2  0 \n";
        if (!cmpString.equals(bBoardString)) System.out.println(bBoardString);
        assert(!aBoard.equals(bBoard));

        // test goal board
        assert aBoard.isGoal();
        assert !bBoard.isGoal();

        // test hamming value
        assert aBoard.hamming() == 0;
        assert bBoard.hamming() == 2;

        // test manhattan
        assert aBoard.manhattan() == 0;

        // System.out.println("" + bBoard.manhattan());
        int bManhattan = bBoard.manhattan();
        assert bBoard.manhattan() == 4;

    }
}