package com.leftisttachyon.tetris.tests;

/**
 * Just probing the behavior of for each loops
 * @author Jed Wang
 */
public class ForEachTest {
    /**
     * The main method; where to run tests
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i : create()) {
            System.out.println(i);         
        }
    }
    
    /**
     * A sample creation of an array
     * @return an array
     */
    private static int[] create() {
        System.out.println("Created");
        return new int[]{1, 2, 3};
    }
}