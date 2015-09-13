/*----------------------------------------------------------------
 *  Author:        Alex Shcherbakov
 *  Written:       8/9/2015
 *  Last updated:  12/9/2015
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats
 *
 *  This programm calculates mean, standard derivation and 95% confidence
 *  interval of percolation for percolation model size N
 *
 *  % java PercolationStats 200 100
 *  treshhold  = 0.607700
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    //private double meanThreshold;
    //private double stddevThreshold;
    private double[] results;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new java.lang.IllegalArgumentException();

        results = new double[T];
        int tryCounter;
        double threshold;

        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            tryCounter = 0;
            while (true) {
                int k = StdRandom.uniform(N) + 1;
                int m = StdRandom.uniform(N) + 1;
                if (percolation.isOpen(k, m))
                    continue;
                percolation.open(k, m);
                tryCounter++;
                if (percolation.percolates())
                    break;
            }
            threshold = Double.valueOf(tryCounter) / (N * N);
            results[i] = threshold;
        }
    }

    // test client (described below)
    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean                    = %f\n", stats.mean());
        StdOut.printf("stddev                  = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", stats.confidenceLo(), stats.confidenceHi());
        double time = stopwatch.elapsedTime();
        StdOut.printf("test time               = %f sec", time);
    }

    // sample mean of percolation threshold
    public double mean() {
        //meanThreshold =  StdStats.mean(results);
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        //stddevThreshold = StdStats.stddev(results);
        return StdStats.stddev(results);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - 1.96 * stddev() / Math.sqrt(results.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + 1.96 * stddev() / Math.sqrt(results.length));
    }
}
