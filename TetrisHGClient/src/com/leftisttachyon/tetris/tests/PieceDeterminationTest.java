package com.leftisttachyon.tetris.tests;

import com.leftisttachyon.tetris.tetrominos.Tetromino;
import com.leftisttachyon.tetris.tetrominos.TetrominoFactory;
import com.leftisttachyon.tetris.tetrominos.ars.ARSTetrominoFactory;
import com.leftisttachyon.tetris.tetrominos.ars.ARS_I;
import java.util.Random;

/**
 * This speed test compares the speed of using the {@code instanceof} operator
 * versus checking for string equality
 *
 * @author Jed Wang
 */
public class PieceDeterminationTest {
    /**
     * The main method; where the tests are run
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TetrominoFactory factory = ARSTetrominoFactory.getTetrominoFactory();
        
        System.out.print("Generating... ");
        final int testLength = 1_000_000;
        Tetromino[] tetrominos = new Tetromino[testLength];
        String[] types = {"I", "J", "L", "O", "S", "T", "Z"};
        Random r = new Random();
        for (int i = 0; i < testLength; i++) {
            tetrominos[i] = factory.createTetrominoOf(types[r.nextInt(7)]);
        }
        System.out.println("Generated");
        
        double start, total;
        boolean[] bools = new boolean[testLength];
        
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            bools[i] = tetrominos[i] instanceof ARS_I;
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("instanceof: %.4f ms%n", total);
        
        bools = new boolean[testLength];
        start = System.nanoTime();
        for (int i = 0; i < testLength; i++) {
            bools[i] = tetrominos[i].getType().equals("I");
        }
        total = System.nanoTime() - start;
        total /= 1_000_000;
        System.out.printf("String checking: %.4f ms%n", total);
    }
}
