package com.github.leftisttachyon.tetris.tetrominos.ars;

import com.github.leftisttachyon.tetris.MinoStyle;
import static com.github.leftisttachyon.tetris.MinoStyle.*;
import com.github.leftisttachyon.util.TetrisUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import net.coobird.thumbnailator.Thumbnails;

/**
 * The TGM style of minos
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class TGMMinoStyle extends MinoStyle {

    /**
     * No instantiation for you!
     */
    private TGMMinoStyle() {
        imageCache = new HashMap[9];
    }

    /**
     * Returns an instance of a TGMMinoStyle
     *
     * @return an instance of a TGMMinoStyle
     */
    public static TGMMinoStyle getMinoStyle() {
        return SINGLETON;
    }

    /**
     * The one and only.
     */
    private static final TGMMinoStyle SINGLETON = new TGMMinoStyle();

    /**
     * An image of a blue mino
     */
    private static final Image BLUE_MINO;

    /**
     * An image of a cyan mino
     */
    private static final Image CYAN_MINO;

    /**
     * An image of a flashing/garbage mino
     */
    private static final Image GREY_MINO;

    /**
     * An image of a green mino
     */
    private static final Image GREEN_MINO;

    /**
     * An image of an orange mino
     */
    private static final Image ORANGE_MINO;

    /**
     * An image of a purple mino
     */
    private static final Image PURPLE_MINO;

    /**
     * An image of a red mino
     */
    private static final Image RED_MINO;

    /**
     * An image of a yellow mino
     */
    private static final Image YELLOW_MINO;

    static {
        BufferedImage temp;

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/blue.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        BLUE_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/cyan.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        CYAN_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/lock.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        GREY_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/green.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        GREEN_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/orange.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ORANGE_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/purple.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PURPLE_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/red.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        RED_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);

        temp = null;
        try {
            temp = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/yellow.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        YELLOW_MINO = temp.getScaledInstance(MINO_SIZE, MINO_SIZE, Image.SCALE_SMOOTH);
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
        }
    }

    /*@Override
    public void drawTetromino(Graphics2D g2D, int x_, int y_, int width,
            Tetromino t) {
        int[][] upState = t.getUpState();
        int tempWidth = width / 4;
        for (int i = 0, x = x_; i < 4; i++, x += tempWidth) {
            for (int j = 0, y = y_; j < 4; j++, y += tempWidth) {
                int color = upState[j][i];

                switch (color) {
                    case BLUE:
                        g2D.drawImage(BLUE_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case CYAN:
                        g2D.drawImage(CYAN_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case GREEN:
                        g2D.drawImage(GREEN_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case ORANGE:
                        g2D.drawImage(ORANGE_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case PURPLE:
                        g2D.drawImage(PURPLE_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case RED:
                        g2D.drawImage(RED_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                    case YELLOW:
                        g2D.drawImage(YELLOW_MINO, x, y, tempWidth, tempWidth, null);
                        break;
                }
            }
        }
    }*/
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
     * A cache of resized images
     */
    private final HashMap<Integer, Image>[] imageCache;

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

        // HashMap<Integer, Image> colorCache = ;
        if (imageCache[color - 1] == null) {
            imageCache[color - 1] = new HashMap<>();
        }

        if (imageCache[color - 1].containsKey(size)) {
            return imageCache[color - 1].get(size);
        } else {
            try {
                BufferedImage file;
                switch (color) {
                    case BLUE:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/blue.png");
                        break;
                    case CYAN:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/cyan.png");
                        break;
                    case GREEN:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/green.png");
                        break;
                    case GREY:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/lock.png");
                        break;
                    case ORANGE:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/orange.png");
                        break;
                    case PURPLE:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/purple.png");
                        break;
                    case RED:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/red.png");
                        break;
                    case YELLOW:
                        file = TetrisUtils.getResource("/com/github/leftisttachyon/tetris/resources/tgm/yellow.png");
                        break;
                    default:
                        return null;
                }

                BufferedImage output = Thumbnails.of(file).height(size).asBufferedImage();

                imageCache[color - 1].put(size, output);

                return output;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
