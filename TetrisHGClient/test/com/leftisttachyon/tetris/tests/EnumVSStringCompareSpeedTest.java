package com.github.leftisttachyon.tetris.tests;

/**
 * A test that compares whether comparing Enums or Strings is faster.
 *
 * @author Jed Wang
 */
public class EnumVSStringCompareSpeedTest {
    /**
     * Representation of north
     */
    private static final String NORTH = "N";
    
    /**
     * Representation of south
     */
    private static final String SOUTH = "S";
    
    /**
     * Representation of east
     */
    private static final String EAST = "E";
    
    /**
     * Representation of west
     */
    private static final String WEST = "W";
    
    /**
     * Representation of all of the directions
     */
    private static final String[] DIRECTIONS = {NORTH, EAST, SOUTH, WEST};
    
    /**
     * The main method; where to run the tests.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] stringDirs;
        Enum[] enumDirs;
        
        System.out.print("Generating... ");
        final int testLength = 100_000;
        stringDirs = new String[testLength];
        enumDirs = new Enum[testLength];
        
        for (int i = 0; i < testLength; i++) {
            int idx = (int) (Math.random() * 4);
            stringDirs[i] = DIRECTIONS[idx];
            enumDirs[i] = Enum.values()[idx];
        }
        System.out.println("Generated");
        
        double start, total;
        boolean[] results = new boolean[testLength];
        
        String sN = NORTH;
        
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            results[i] = sN.equals(stringDirs[i]);
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Strings: %.3f ms%n", total);
        
        results = new boolean[testLength];
        Enum eN = Enum.NORTH;
        
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            results[i] = eN == enumDirs[i];
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("Enums: %.3f ms%n", total);
    }
    
    /**
     * An example enum
     */
    public static enum Enum {
        NORTH, EAST, SOUTH, WEST;
    }
}
