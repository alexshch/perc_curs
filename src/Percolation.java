/**
 * Created by SuN on 08.09.2015.
 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Random;


public class Percolation {
    // create N-by-N grid, with all sites blocked
    private WeightedQuickUnionUF openSitesGrid;
    private boolean[][] emptyOpenSites;
    private int SizeN;
    public Percolation(int N) {
        if (N <= 0) throw new IndexOutOfBoundsException("too low N");

        openSitesGrid = new WeightedQuickUnionUF(N*N);
        emptyOpenSites = new boolean[N][N];
        SizeN = N;

        }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {

        int currentSite = transformDecardCoordToUnionCoord(i,j);
        if (!isOpen(i,j)){
            int[] neighbors = openNeighborSites(i,j);
            for (int k=0; k<4; k++ ){
                if (neighbors[k] != -1){
                    openSitesGrid.union(currentSite, neighbors[k]);
                }
            }

            emptyOpenSites[i-1][j-1] = true;

        }
    }
    // transform Decard coordinates to element number in WeightedQuickUnionUF
    private int transformDecardCoordToUnionCoord(int i, int j){
        return (i-1)*SizeN+(j-1);
    }

    //return a naighbours sites (-1 in the absent)
    private int[] openNeighborSites(int i,int j){
        //{left, right, up, down}
        int[] neighbors = new int[4];


        neighbors[0] = (j != 1) ? transformDecardCoordToUnionCoord(i, j - 1) : -1;
        if (neighbors[0] != -1) {
            if (!isOpen(i, j - 1)) {
                neighbors[0] = -1;
            }
        }
        neighbors[1] = (j != SizeN) ? transformDecardCoordToUnionCoord(i, j + 1) : -1;
        if (neighbors[1] != -1) {
            if (!isOpen(i, j + 1)) {
                neighbors[1] = -1;
            }
        }
        neighbors[2] = (i != 1) ? transformDecardCoordToUnionCoord(i - 1, j) : -1;
        if (neighbors[2] != -1) {
            if (!isOpen(i - 1, j)) {
                neighbors[2] = -1;
            }
        }
        neighbors[3] = (i != SizeN) ? transformDecardCoordToUnionCoord(i + 1, j) : -1;
        if (neighbors[3] != -1) {
            if (!isOpen(i + 1, j)) {
                neighbors[3] = -1;
            }
        }
        return neighbors;
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        return emptyOpenSites[i-1][j-1];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        int currentSite = transformDecardCoordToUnionCoord(i,j);
        for (int k = 1; k < SizeN+1; k++){
            int topSite =  transformDecardCoordToUnionCoord(1,k);
            if (isOpen(1,k) && openSitesGrid.connected(topSite,currentSite)){
                return true;
            }
        }
        return false;
        //return  fullOpenSites[i-1][j-1];
    }

    // does the system percolate?
    public boolean percolates() {
        for(int i=1; i<SizeN+1; i++ ){
            if (isFull(SizeN,i))
                return true;
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {

        int N = 100;
        int trys = 0;
        Percolation percolation = new Percolation(N);
        //StdRandom random = new StdRandom();
        while(true){
            int i = StdRandom.uniform(N)+1;
            int j = StdRandom.uniform(N)+1;
            if (percolation.isOpen(i, j))
                continue;
            percolation.open(i,j);
            trys++;
            if (percolation.percolates())
                break;
        }

        System.out.println("final trys = "+String.valueOf(trys));
        System.out.println("treshhold = "+String.valueOf(Double.valueOf(trys)/(N*N)));
    }
}
