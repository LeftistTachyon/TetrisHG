package com.leftisttachyon.tetris.tests;

/**
 * A test to find the speed difference between pre-increment and post-increment
 *
 * @author Jed Wang
 */
public class IncrementTest {

    /**
     * The main method; where to run the tests
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int testLength = 1_000_000_000;
        double start, total;
        int cnt = 0;

        start = System.nanoTime();
        for (; cnt < testLength; cnt++) {
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Post: %.3f ms%n", total);
        
        cnt = 0;
        
        start = System.nanoTime();
        for (; cnt < testLength; ++cnt) {
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Pre:  %.3f ms%n", total);
    }
}
