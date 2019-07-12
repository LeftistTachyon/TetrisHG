package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.tetrominos.AbstractTetromino;
import static com.leftisttachyon.tetris.tetrominos.AbstractTetromino.*;
import javax.swing.JPanel;

/**
 * A class that represents a playing field
 *
 * @author Jed Wang
 */
public class Matrix extends JPanel {

    /**
     * The actual matrix
     */
    private int[][] matrix;

    /**
     * Creates a new Matrix.
     */
    public Matrix() {
        matrix = new int[40][20];
    }

    /**
     * Finds and returns the value of the block/mino at the specified row and
     * column. If the row or column are out of bounds, then it returns the value
     * of WALL.
     *
     * @param r the row of the matrix to find
     * @param c the column of the matrix to find
     * @return the value of the wanted block/mino
     * @see AbstractTetromino#WALL
     */
    public int getBlock(int r, int c) {
        if (r < 0 || r >= matrix.length || c < 0 || c >= matrix[r].length) {
            return WALL;
        } else {
            return matrix[r][c];
        }
    }

    /**
     * Copies a portion of the internal matrix to a new matrix. Uses
     * {@code getBlock} to determine the value of each block.
     *
     * @param r the row of the top left corner
     * @param c the column of the top left corner
     * @param w the width of the wanted matrix
     * @param h the height of the wanted matrix
     * @return the copied portion of the matrix
     * @see #getBlock(int, int) 
     */
    public int[][] getSection(int r, int c, int w, int h) {
        int[][] output = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                output[i][j] = getBlock(r + i, c + j);
            }
        }
        return output;
    }
}
