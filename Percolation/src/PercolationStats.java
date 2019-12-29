import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private int numOfTrials;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n or trials is smaller than 0");
        double[] threshold;
        threshold = new double[trials];
        numOfTrials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                percolation.open(row, col);
            }
            threshold[i] = percolation.numberOfOpenSites();
            threshold[i] = threshold[i] / (n*n);
        }
        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
    }

    public double mean() { return mean; }

    public double stddev() { return stddev; }

    public double confidenceLo() {
        return mean - stddev * 1.96 / Math.sqrt(numOfTrials);
    }
    public double confidenceHi() { return mean + stddev * 1.96 / Math.sqrt(numOfTrials); }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println("mean    = " + stats.mean());
        StdOut.println("stddev  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + "[ " + stats.confidenceLo()
                          + "  " + stats.confidenceHi() + "]");
        }
}
