import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95_FACTOR = 1.96;
    private final double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("invalid n or trials");
        }

        // init
        results = new double[trials];

        // independent trials
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            boolean done = false;
            while (!done) {
                done = iterate(perc, n);
            }
            // Percolates
            double numberOfOpenSites = (double) perc.numberOfOpenSites();
            double result = numberOfOpenSites / (n * n);
            results[i] = result;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95_FACTOR * stddev() / Math.sqrt(results.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95_FACTOR * stddev() / Math.sqrt(results.length);
    }

    /*
    private helper methods
    */
    private boolean iterate(Percolation perc, int dimension) {
        // randomly open a site
        int randomRow, randomCol;
        do {
            randomRow = StdRandom.uniform(1, dimension+1);
            randomCol = StdRandom.uniform(1, dimension+1);
        } while (perc.isOpen(randomRow, randomCol)); 

        perc.open(randomRow, randomCol);

        if (perc.percolates()) {
            return true;
        }

        return false;
    }
    
    // test client (see below)
    public static void main(String[] args) {
        int n, trials;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(n, trials);

            // print out results
            StdOut.println("mean\t\t\t= " + ps.mean());
            StdOut.println("stddev\t\t\t= " + ps.stddev());
            StdOut.println("95% confidence interval\t= [" +
                ps.confidenceLo() + ", " + ps.confidenceHi() +
                "]");
        }
    }
}