package com.leftisttachyon.tetris.tetrominos;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An abstract class that can be implemented to create a Tetromino factory.
 *
 * @author Jed Wang
 * @param <T> the specific type of tetromino being created
 * @since 0.9.0
 */
public abstract class TetrominoFactory<T extends Tetromino> {

    /**
     * Creates a new Tetromino of the given type
     *
     * @param type the type of Tetromino to create
     * @return the new Tetromino
     */
    public abstract T createTetrominoOf(String type);

    /**
     * Creates a bag from the given String. The String should contain all 7
     * tetrominos in a random order. They all need to be in uppercase.
     *
     * @param tetrominos the String to generate a bag from
     * @return the generated bag
     */
    public List<T> createBagOf(String tetrominos) {
        List<T> output = new LinkedList<>();
        for (String s : tetrominos.split("")) {
            output.add(createTetrominoOf(s));
        }
        return output;
    }

    /**
     * Creates a random bag of tetrominos. Inside the list is all 7 tetrominos,
     * in a random order.
     *
     * @return a random bag of tetrominos
     */
    public List<T> createRandomBag() {
        List<T> output = new LinkedList<>();

        output.add(createTetrominoOf("I"));
        output.add(createTetrominoOf("J"));
        output.add(createTetrominoOf("L"));
        output.add(createTetrominoOf("O"));
        output.add(createTetrominoOf("S"));
        output.add(createTetrominoOf("T"));
        output.add(createTetrominoOf("Z"));

        Collections.shuffle(output);

        return output;
    }
}
