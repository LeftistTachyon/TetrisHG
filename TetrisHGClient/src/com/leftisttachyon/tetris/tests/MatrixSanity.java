package com.leftisttachyon.tetris.tests;

import com.leftisttachyon.util.TetrisUtils;

/**
 * A sanity check to make sure my topmost and other code works
 *
 * @author Jed Wang
 */
public class MatrixSanity {

    /**
     * The main method; where to run the tests
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] toCheck = {
            {0, 0, 0, 0}, 
            {1, 1, 1, 0}, 
            {0, 0, 1, 0}, 
            {0, 0, 0, 0}
        };
        // should be 1 2 0 2
        System.out.println("TOP:    " + TetrisUtils.highestRow(toCheck));
        System.out.println("BOTTOM: " + TetrisUtils.lowestRow(toCheck));
        System.out.println("LEFT:   " + TetrisUtils.leftmostCol(toCheck));
        System.out.println("RIGHT:  " + TetrisUtils.rightmostCol(toCheck));
    }
}
