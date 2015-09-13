/*----------------------------------------------------------------
 *  Author:        Alex Shcherbakov
 *  Written:       8/9/2015
 *  Last updated:  12/9/2015
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *
 *  This programm calculates probability of percolation for percolation model size N
 *
 *  % java Percolation 100
 *  treshhold  = 0.607700
 *----------------------------------------------------------------*/

//import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.QuickFindUF;


public class Percolation {

    private WeightedQuickUnionUF openSitesGrid;     //union of open sites
    private WeightedQuickUnionUF fullSitesGrid;     //union of open sites
    private boolean[][] emptyOpenSites;             //map of open sites
    private int sizeN;                              //grid size
    private int top;                                //top element in WeightedQuickUnionUF
    private int bottom;                             //bottom element in WeightedQuickUnionUF

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("too low N");

        openSitesGrid = new WeightedQuickUnionUF(N * N + 2); //openSitesGrid [N*N-2] - top, openSitesGrid [N*N-1] -bottom
        fullSitesGrid = new WeightedQuickUnionUF(N * N + 1);
        emptyOpenSites = new boolean[N][N];
        sizeN = N;
        this.top = N * N;
        this.bottom = N * N + 1;

    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (i <= 0 || i > sizeN) throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > sizeN) throw new IndexOutOfBoundsException("column index j out of bounds");

        int currentSite = transformDecardCoordToUnionCoord(i, j); //the site which is opening
        if (!isOpen(i, j)) {
            int[] neighbors = openNeighbourSites(i, j);
            if (i == 1) {
                openSitesGrid.union(currentSite, this.top);
                fullSitesGrid.union(currentSite, this.top);
            }
            if (i == sizeN)
                openSitesGrid.union(currentSite, bottom);
            for (int k = 0; k < 4; k++) {
                if (neighbors[k] != -1) {
                    openSitesGrid.union(currentSite, neighbors[k]);
                    fullSitesGrid.union(currentSite, neighbors[k]);
                }
            }


            emptyOpenSites[i - 1][j - 1] = true;

        }
    }

    // transform Decard coordinates to element number in WeightedQuickUnionUF
    private int transformDecardCoordToUnionCoord(int i, int j) {
        return (i - 1) * sizeN + (j - 1);
    }

    //return a massive of neighbour sites (-1 in the absent)
    private int[] openNeighbourSites(int i, int j) {

        int[] neighbours = new int[4]; //{left, right, up, down} neighbours of site

        //neighbours[0] = (j != 1) ? transformDecardCoordToUnionCoord(i, j - 1) : -1;
        if (j != 1) {
            neighbours[0] = transformDecardCoordToUnionCoord(i, j - 1);
        } else {
            neighbours[0] = -1;
        }
        if (neighbours[0] != -1) {
            if (!isOpen(i, j - 1)) {
                neighbours[0] = -1;
            }
        }
        //neighbours[1] = (j != sizeN) ? transformDecardCoordToUnionCoord(i, j + 1) : -1;
        if (j != sizeN) {
            neighbours[1] = transformDecardCoordToUnionCoord(i, j + 1);
        } else {
            neighbours[1] = -1;
        }
        if (neighbours[1] != -1) {
            if (!isOpen(i, j + 1)) {
                neighbours[1] = -1;
            }
        }
        //neighbours[2] = (i != 1) ? transformDecardCoordToUnionCoord(i - 1, j) : -1;
        if (i != 1) {
            neighbours[2] = transformDecardCoordToUnionCoord(i - 1, j);
        } else {
            neighbours[2] = -1;
        }
        if (neighbours[2] != -1) {
            if (!isOpen(i - 1, j)) {
                neighbours[2] = -1;
            }
        }
        //neighbours[3] = (i != sizeN) ? transformDecardCoordToUnionCoord(i + 1, j) : -1;
        if (i != sizeN) {
            neighbours[3] = transformDecardCoordToUnionCoord(i + 1, j);
        } else {
            neighbours[3] = -1;
        }
        if (neighbours[3] != -1) {
            if (!isOpen(i + 1, j)) {
                neighbours[3] = -1;
            }
        }
        return neighbours;
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {

        if (i <= 0 || i > sizeN)
            throw new java.lang.IndexOutOfBoundsException("i is out of range 1..N");
        if (j <= 0 || j > sizeN)
            throw new java.lang.IndexOutOfBoundsException("j is out of range 1..N");

        return emptyOpenSites[i - 1][j - 1];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        if (i <= 0 || i > sizeN)
            throw new java.lang.IndexOutOfBoundsException("i is out of range 1..N");
        if (j <= 0 || j > sizeN)
            throw new java.lang.IndexOutOfBoundsException("j is out of range 1..N");

        int currentSite = transformDecardCoordToUnionCoord(i, j);

        return fullSitesGrid.connected(this.top, currentSite);
    }

    // does the system percolate?
    public boolean percolates() {

        if (openSitesGrid.connected(this.top, this.bottom))
            return true;

        return false;
    }

    // test client (optional)
    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        int tryCounter = 0;
        Percolation percolation = new Percolation(N);
        //StdRandom random = new StdRandom();
        while (true) {
            int i = StdRandom.uniform(N) + 1;
            int j = StdRandom.uniform(N) + 1;
            if (percolation.isOpen(i, j))
                continue;
            percolation.open(i, j);
            tryCounter++;
            if (percolation.percolates())
                break;
        }

        //StdOut.printf("treshhold  = %f\n", Double.valueOf(tryCounter / (N * N)));
    }
}