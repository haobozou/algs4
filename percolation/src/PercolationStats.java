import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_INTERVAL_RADIUS = 1.96;
    private final int trials;
    private final double[] fractions;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        this.fractions = new double[this.trials];
        for (int i = 0; i < this.trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                percolation.open(row, col);
            }
            this.fractions[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(this.fractions);
    }

    public double stddev() {
        return StdStats.stddev(this.fractions);
    }

    public double confidenceLo() {
        return mean() - CONFIDENCE_INTERVAL_RADIUS * stddev() / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return mean() + CONFIDENCE_INTERVAL_RADIUS * stddev() / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}