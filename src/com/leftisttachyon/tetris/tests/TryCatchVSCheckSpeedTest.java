package com.leftisttachyon.tetris.tests;

import java.util.Random;

/**
 * A test that compares the speed of manually checking out of bounds or just
 * relying on try/catch.
 *
 * @author Jed Wang
 */
public class TryCatchVSCheckSpeedTest {
    /**
     * The main method; where to run the tests.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int width = 10_000;
        final int height = 10_000;
        final int[][] mat = new int[height][width];
        Random rand = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mat[i][j] = rand.nextInt(25);
            }
        }
        double start, total;
        
        int[][] copy = new int[height][width];
        
        start = System.nanoTime();
        for (int i = 0; i < height; i++) {
            int r = i - height / 2;
            for (int j = 0; j < width; j++) {
                try {
                    copy[i][j] = mat[r][j];
                } catch (IndexOutOfBoundsException ioobe) {
                    copy[i][j] = -1;
                }
            }
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Try/catch: %.4f ms%n", total);
        
        copy = new int[height][width];
        start = System.nanoTime();
        for (int i = 0; i < height; i++) {
            int r = i - height / 2;
            for (int j = 0; j < width; j++) {
                if(r < 0 || r >= height || j < 0 || j >= width) {
                    copy[i][j] = -1;
                } else {
                    copy[i][j] = mat[r][j];
                }
            }
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Manual: %.4f ms%n", total);
    }
}
