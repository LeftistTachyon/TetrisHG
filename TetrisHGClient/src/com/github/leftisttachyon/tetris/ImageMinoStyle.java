package com.github.leftisttachyon.tetris;

import static com.github.leftisttachyon.tetris.MinoStyle.BLUE;
import static com.github.leftisttachyon.tetris.MinoStyle.CYAN;
import static com.github.leftisttachyon.tetris.MinoStyle.GREEN;
import static com.github.leftisttachyon.tetris.MinoStyle.GREY;
import static com.github.leftisttachyon.tetris.MinoStyle.MINO_SIZE;
import static com.github.leftisttachyon.tetris.MinoStyle.ORANGE;
import static com.github.leftisttachyon.tetris.MinoStyle.PURPLE;
import static com.github.leftisttachyon.tetris.MinoStyle.RED;
import static com.github.leftisttachyon.tetris.MinoStyle.YELLOW;
import com.github.leftisttachyon.util.TetrisUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import net.coobird.thumbnailator.Thumbnails;

/**
 * An abstract class that draws individual minos as images.
 *
 * @author Jed Wang
 *
 * @since 0.9.2
 */
public abstract class ImageMinoStyle extends MinoStyle {

    /**
     * A cache of resized images
     */
    private final HashMap<Integer, Image>[] imageCache;

    /**
     * An image of a blue mino
     */
    protected final Image BLUE_MINO;

    /**
     * An image of a cyan mino
     */
    protected final Image CYAN_MINO;

    /**
     * An image of a flashing/garbage mino
     */
    protected final Image GREY_MINO;

    /**
     * An image of a green mino
     */
    protected final Image GREEN_MINO;

    /**
     * An image of an orange mino
     */
    protected final Image ORANGE_MINO;

    /**
     * An image of a purple mino
     */
    protected final Image PURPLE_MINO;

    /**
     * An image of a red mino
     */
    protected final Image RED_MINO;

    /**
     * An image of a yellow mino
     */
    protected final Image YELLOW_MINO;

    /**
     * No instantiation for you!
     */
    protected ImageMinoStyle() {
        imageCache = new HashMap[9];

        BLUE_MINO = getImageScaled(getPath(BLUE), MINO_SIZE, MINO_SIZE);

        CYAN_MINO = getImageScaled(getPath(CYAN), MINO_SIZE, MINO_SIZE);

        GREY_MINO = getImageScaled(getPath(GREY), MINO_SIZE, MINO_SIZE);

        GREEN_MINO = getImageScaled(getPath(GREEN), MINO_SIZE, MINO_SIZE);

        ORANGE_MINO = getImageScaled(getPath(ORANGE), MINO_SIZE, MINO_SIZE);

        PURPLE_MINO = getImageScaled(getPath(PURPLE), MINO_SIZE, MINO_SIZE);

        RED_MINO = getImageScaled(getPath(RED), MINO_SIZE, MINO_SIZE);

        YELLOW_MINO = getImageScaled(getPath(YELLOW), MINO_SIZE, MINO_SIZE);
    }

    /**
     * Returns the path of the image of an mino of the given color
     *
     * @param color the color to get the path of
     * @return the path of the image of an mino of the given color
     */
    protected abstract String getPath(int color);

    /**
     * Returns a scaled version of the image at the given path
     *
     * @param path the path of the image
     * @param width the width of the output image
     * @param height the height of the output image
     * @return a scaled version of the image
     */
    public static Image getImageScaled(String path, int width, int height) {
        Image temp;
        try {
            temp = TetrisUtils.getResource(path);
            return temp.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void drawMino(Graphics2D g2D, int x, int y, int color) {
        switch (color) {
            case BLUE:
                g2D.drawImage(BLUE_MINO, x, y, null);
                break;
            case CYAN:
                g2D.drawImage(CYAN_MINO, x, y, null);
                break;
            case GREY:
                g2D.drawImage(GREY_MINO, x, y, null);
                break;
            case GREEN:
                g2D.drawImage(GREEN_MINO, x, y, null);
                break;
            case ORANGE:
                g2D.drawImage(ORANGE_MINO, x, y, null);
                break;
            case PURPLE:
                g2D.drawImage(PURPLE_MINO, x, y, null);
                break;
            case RED:
                g2D.drawImage(RED_MINO, x, y, null);
                break;
            case YELLOW:
                g2D.drawImage(YELLOW_MINO, x, y, null);
                break;
            default:
                g2D.setColor(Color.WHITE);
                g2D.fillRect(x, y, MINO_SIZE, MINO_SIZE);
                g2D.setColor(Color.RED);
                g2D.drawString("?", x, y + MINO_SIZE);
                break;
            case EMPTY:
                break;
        }
    }
    
    @Override
    public void drawMino(Graphics2D g2D, int x, int y, int size, int color) {
        if (size == MINO_SIZE) {
            drawMino(g2D, x, y, color);
            return;
        }

        if (color != 0) {
            g2D.drawImage(getScaledMino(color, size), x, y, null);
        }
    }
    
    /**
     * Gets a scaled instance of an image of a mino
     *
     * @param color the color of the mino to get
     * @param size the size of the mino
     * @return the scaled instance of an image
     */
    private Image getScaledMino(int color, int size) {
        if (color < 1 || color > 9) {
            return null;
        }
        
        if (imageCache[color - 1] == null) {
            imageCache[color - 1] = new HashMap<>();
        }

        if (imageCache[color - 1].containsKey(size)) {
            return imageCache[color - 1].get(size);
        } else {
            try {
                BufferedImage file = TetrisUtils.getResource(getPath(color));

                BufferedImage output = Thumbnails.of(file)
                        .height(size)
                        .asBufferedImage();

                imageCache[color - 1].put(size, output);

                return output;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
