import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import java.lang.IllegalArgumentException;

public class Percolation {
    private int dimension;
    private WeightedQuickUnionUF gridUF;
    private boolean[] grid;
    private int openSites;
    private WeightedQuickUnionUF fullUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("index out of bound");

        //StdOut.println("Initializing with n = " + n);

        // Initialization begins
        dimension = n;
        grid = new boolean[n*n+2];
        for (int i = 1; i < n*n+1; i++) {
            grid[i] = false;
        }
        gridUF = new WeightedQuickUnionUF(n*n+2);
        fullUF = new WeightedQuickUnionUF(n*n+1);
        openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        //StdOut.println("Try to open (" + row + ", " + col + "");
        if (validate2DIndex(row, col)) {
            //StdOut.println("Valid input. Grid opened.");
            int gridIndex = index(row, col);
            if (!grid[gridIndex]) openSites++;
            grid[gridIndex] = true;
            unionAfterOpen(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (validate2DIndex(row, col)) {
            //StdOut.println("(" + row + ", " + col + ") - " + 
                //(grid[index(row, col)] ? "Opened" : "Closed"));
            return grid[index(row, col)];
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        /*
        if (false && row == 1 && col == 1) {
            StdOut.println("(" + row + ", " + col + ") - " + 
                (grid[index(row, col)] ? "Opened" : "Closed"));
            StdOut.println("(" + row + ", " + col + ") - " + 
                (gridUF.find(0) == gridUF.find(index(row, col)) 
                    ? "Full" : "Not full"));
        }
        */
        if (validate2DIndex(row, col)) {
            return fullUF.find(0) == fullUF.find(index(row, col));
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return gridUF.find(0) == gridUF.find(1 + dimension * dimension);
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
        unionLeft(row, col);
        unionRight(row, col);
        unionUp(row, col);
        unionDown(row, col);
    }

    // check adjacent grids and union if any is openj
    private void unionLeft(int row, int col) {
        if (col == 1 || !isOpen(row, col-1)) {
            return;
        }

        int left = index(row, col-1);
        int curr = index(row, col);

        gridUF.union(left, curr);
        fullUF.union(left, curr);
    }
    private void unionRight(int row, int col) {
        if (col == dimension || !isOpen(row, col+1)) {
            return;
        }

        int right = index(row, col+1);
        int curr = index(row, col);

        gridUF.union(right, curr);
        fullUF.union(right, curr);
    }
    private void unionUp(int row, int col) {
        int curr = index(row, col);
        if (row == 1) {
            gridUF.union(0, curr);
            fullUF.union(0, curr);
        } else if (isOpen(row-1, col)) {
            int up = index(row-1, col);
            gridUF.union(curr, up);
            fullUF.union(curr, up);
        }
    }
    private void unionDown(int row, int col) {
        int curr = index(row, col);
        if (row == dimension) {
            gridUF.union(dimension*dimension+1, curr);
        } else if (isOpen(row+1, col)) {
            int down = index(row+1, col);
            gridUF.union(down, curr);
            fullUF.union(down, curr);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        if (args.length == 1) {
            int n = Integer.parseInt(args[0]);
            Percolation perc = new Percolation(n);
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (perc.isOpen(i, j)) {
                        StdOut.println("i - " + i + "; open - " + 
                            perc.isOpen(i, j));
                    }
                }
            }
        }
    }
}