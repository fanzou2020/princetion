import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshold;
    private Percolation percolation;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while(!percolation.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                percolation.open(row, col);
            }
            threshold[i] = percolation.numberOfOpenSites();
            StdOut.println(threshold[i]);
            threshold[i] = threshold[i] / (n*n);
        }
    }

    public double mean() { return StdStats.mean(threshold); }

    public double stddev() { return StdStats.stddev(threshold); }

    public double confidenceLo() {
        return mean() - stddev() * 1.96 / Math.sqrt(threshold.length);
    }
    public double confidenceHi() {
        return mean() + stddev() * 1.96 / Math.sqrt(threshold.length);
    }

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
