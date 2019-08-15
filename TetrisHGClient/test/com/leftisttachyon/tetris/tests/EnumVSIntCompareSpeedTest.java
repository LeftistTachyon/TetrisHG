package com.github.leftisttachyon.tetris.tests;

import com.github.leftisttachyon.tetris.tests.EnumVSStringCompareSpeedTest.Enum;
import static com.github.leftisttachyon.tetris.tests.EnumVSStringCompareSpeedTest.Enum.NORTH;

/**
 * A test that compares whether comparing Enums or Integers is faster.
 *
 * @author Jed Wang
 */
public class EnumVSIntCompareSpeedTest {
    
    /**
     * Representation of south
     */
    private static final int SOUTH_I = 2;
    
    /**
     * Representation of east
     */
    private static final int EAST_I = 1;
    
    /**
     * Representation of north
     */
    private static final int NORTH_I = 0;
    
    /**
     * Representation of west
     */
    private static final int WEST_I = 3;
    
    /**
     * All directions
     */
    private static final int[] DIRECTIONS_I = {NORTH_I, EAST_I, SOUTH_I, WEST_I};
    
    /**
     * The main method; where to run the tests.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Enum[] enumDirs;
        int[] intDirs;
        
        System.out.print("Generating... ");
        final int testLength = 100_000;
        enumDirs = new Enum[testLength];
        intDirs = new int[testLength];
        
        for (int i = 0; i < testLength; i++) {
            int idx = (int) (Math.random() * 4);
            enumDirs[i] = Enum.values()[idx];
            intDirs[i] = DIRECTIONS_I[idx];
        }
        System.out.println("Generated");
        
        double start, total;
        boolean[] results = new boolean[testLength];
        
        Enum eN = NORTH;
        
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            results[i] = eN.equals(enumDirs[i]);
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Enums: %.3f ms%n", total);
        
        results = new boolean[testLength];
        int iN = NORTH_I;
        
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            results[i] = iN == intDirs[i];
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("ints: %.3f ms%n", total);
    }
}