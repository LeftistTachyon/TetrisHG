package com.leftisttachyon.tetris;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A mino style that is solid colors.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class BasicMinoStyle implements MinoStyle {

    /**
     * The only one.
     */
    private static final BasicMinoStyle SINGLETON = new BasicMinoStyle();

    /**
     * Returns an instance of a BasicMinoStyle
     *
     * @return an instance of a BasicMinoStyle
     */
    public static BasicMinoStyle getMinoStyle() {
        return SINGLETON;
    }

    /**
     * No creation for you!
     */
    private BasicMinoStyle() {
    }

    /**
     * The color purple
     */
    private static final Color purple = new Color(128, 0, 128);
    
    /**
     * A flashy color
     */
    private static final Color flash = new Color(186, 194, 194);

    @Override
    public void drawMino(Graphics2D g2D, int x, int y, int color) {
        switch (color) {
            case CYAN:
                g2D.setColor(Color.CYAN);
                break;
            case BLUE:
                g2D.setColor(Color.BLUE);
                break;
            case ORANGE:
                g2D.setColor(Color.ORANGE);
                break;
            case YELLOW:
                g2D.setColor(Color.YELLOW);
                break;
            case GREEN:
                g2D.setColor(Color.GREEN);
                break;
            case PURPLE:
                g2D.setColor(purple);
                break;
            case RED:
                g2D.setColor(Color.RED);
                break;
            case FLASH:
                g2D.setColor(flash);
                break;
            default:
                return;
        }

        g2D.fillRect(x, y, MINO_SIZE, MINO_SIZE);
    }

}
