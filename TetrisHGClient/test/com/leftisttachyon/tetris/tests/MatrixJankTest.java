package com.github.leftisttachyon.tetris.tests;

import java.util.Arrays;

/**
 * This class tests the small things about matrices. Hard to explain.
 *
 * @author Jed Wang
 */
public class MatrixJankTest {
    
    private static final int[][] iii = {{4, 5, 6}, {1, 2, 3}};

    /**
     * The main method; where to run the tests.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(iii);
        System.out.println("********************");
        int[][] i1 = {{4, 5, 6}, {1, 2, 3}};
        int[][] i2 = {{4, 5, 6}, {1, 2, 3}};
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i1 == i2);
        System.out.println(Arrays.deepEquals(i1, i2));
        System.out.println("********************");
        int[][] i3 = iii;
        int[][] i4 = iii;
        System.out.println(i3);
        System.out.println(i4);
        System.out.println(i3 == i4);
        System.out.println(iii == i3);
        System.out.println(iii == i4);
        System.out.println(Arrays.deepEquals(i3, i4));
    }
}
