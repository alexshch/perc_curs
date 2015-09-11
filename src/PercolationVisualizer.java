/**
 * Created by SuN on 08.09.2015.
 */
import java.awt.Font;
import java.util.Random;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;


public class PercolationVisualizer {

    // delay in miliseconds (controls animation speed)
    private static final int DELAY = 100;

    // draw N-by-N percolation system
    public static void draw(Percolation perc, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(-.05*N, 1.05*N);
        StdDraw.setYscale(-.05*N, 1.05*N);   // leave a border to write text
        StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);

        // draw N-by-N grid
        int opened = 0;
        for (int row = 1; row <= N; row++) {
            for (int col = 1; col <= N; col++) {
                if (perc.isFull(row, col)) {
                    StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                    opened++;
                }
                else if (perc.isOpen(row, col)) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    opened++;
                }
                else
                    StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledSquare(col - 0.5, N - row + 0.5, 0.45);
            }
        }

        // write status text
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(.25*N, -N*.025, opened + " open sites");
        if (perc.percolates()) StdDraw.text(.75*N, -N*.025, "percolates");
        else                   StdDraw.text(.75*N, -N*.025, "does not percolate");

    }

    public static void main(String[] args) {
        //In in = new In(args[0]);      // input file
        int N = 20;//in.readInt();         // N-by-N percolation system

        // turn on animation mode
        StdDraw.show(0);
        Random rnd = new Random();
        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(N);
        draw(perc, N);
        StdDraw.show(DELAY);
        //while (!in.isEmpty()) {
        while(true){
            //int i = in.readInt();
            int i = rnd.nextInt(N)+1;
            //int j = in.readInt();
            int j = rnd.nextInt(N)+1;
            if (perc.isOpen(i,j))
                continue;
            perc.open(i, j);
            draw(perc, N);
            StdDraw.show(DELAY);
        }
    }
}