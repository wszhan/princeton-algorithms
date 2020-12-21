import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private final int dimension;
    //private final boolean[] grid;
    private final WeightedQuickUnionUF gridUF;
    private boolean percolationFlag;
    //private final WeightedQuickUnionUF fullUF;
    // 8-bit representation: 0000 0000
    // the lower bit for open/blocked: 1 for open and 0 for blocked.
    // e.g. open: 0000 0001 = 1; close: 0000 0000 = 0
    // the 2nd bit for connectivity to any site at the BOTTOM:
    // e.g. 0000 0011 = 3
    // the 3rd bit for connectivity to any site at the TOP:
    // e.g. 0000 0101 = 5
    // percolation equals the state of a set having connectivity 
    // simultaneously to BOTTOM and TOP:
    // e.g. 0000 0111 = 7
    // BE AWARE that if a site is connected to the bottom/top, it is also
    // implicitly open, so the last bit would be 1.
    private char[] connectivityStates;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Illegal initiation.");

        // Initialization begins
        dimension = n;
        connectivityStates = new char[n*n+2];
        percolationFlag = false;
        openSites = 0;
        gridUF = new WeightedQuickUnionUF(n*n+2);

        for (int i = 1; i < n*n+1; i++) {
            connectivityStates[i] = (char) 0;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (validate2DIndex(row, col)) {
            // if the site is already open, do nothing;
            // otherwise, update relevant values
            if (!isOpen(row, col)) {
                int gridIndex = index(row, col);
                char state;
                if (row == 1) state = (char) 5;
                else if (row == dimension) state = (char) 3;
                else state = (char) 1;

                connectivityStates[gridIndex] = state;
                unionAfterOpen(row, col);

                openSites++;
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (validate2DIndex(row, col)) {
            boolean open = connectivityStates[index(row, col)] > 0;
            int state = (int) connectivityStates[index(row, col)];
            return state > 0;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (validate2DIndex(row, col)) {
            int currIndex = index(row, col);
            int rootIndex = gridUF.find(currIndex);
            int rootState = (int) connectivityStates[rootIndex];
            if (rootState >= 5) {
                return true;
            }        
        }
        
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationFlag;
    }

    /*
    private helper methods
    */
    // convert 2d index = 1d index
    private int index(int row, int col) {
        return (row - 1) * dimension + col;
    }
    
    // validate two-dimensional index
    // 1 <= row, col <= dimension
    private boolean validate2DIndex(int row, int col) {
        if (row >= 1 && row <= dimension &&
            col >= 1 && col <= dimension) {
                return true;
            }
        throw new IllegalArgumentException("index out of bound");
    }

    // union after open
    private void unionAfterOpen(int row, int col) {
        // union left
        unionAdjacent(row, col, 0, -1); 
        // union right 
        unionAdjacent(row, col, 0, 1); 
        // union up
        unionAdjacent(row, col, -1, 0);
        // union down
        unionAdjacent(row, col, 1, 0);
    }

    private void unionAdjacent(int row, int col, int verticalOffset, int horizontalOffset) {
        try {
            if (validate2DIndex(row + verticalOffset, col + horizontalOffset)) {
                int adjacentIndex = index(row + verticalOffset, col + horizontalOffset);
                int adjacentRoot = gridUF.find(adjacentIndex);
                char prevAdjacentState = connectivityStates[adjacentRoot];

                if ((int) prevAdjacentState != 0) {
                    // Temp vars for root index in case of losing info
                    // after union (root of one set must be changed)
                    int currIndex = index(row, col);
                    int currRoot = gridUF.find(currIndex);

                    // previous states
                    char prevCurrState = connectivityStates[currRoot];

                    // union and root is updated
                    gridUF.union(currIndex, adjacentIndex);
                    int newRoot = gridUF.find(currIndex);

                    // update states
                    connectivityStates[newRoot] = (char) (
                        prevCurrState | prevAdjacentState
                    );
                    
                    if (connectivityStates[newRoot] == 7) {
                        percolationFlag = true;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        char block = (char) 0; // 0000 0000
        char open = (char) 1; // 0000 0001
        char hasTop = (char) 5; // 0000 0101 implicitly open
        char hasBottom = (char) 3; // 0000 0011 also implicitly open
        // union a normal block with a block connected to the top
        char unionTop = (char) (open | hasTop);
        // union a normal block with a block connected to the bottom 
        char unionBottom = (char) (open | hasBottom);
        StdOut.println((int) unionBottom);
    }
}