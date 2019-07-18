package com.leftisttachyon.tetris;

import com.leftisttachyon.tetris.tetrominos.Tetromino;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A mino style that is solid colors.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class BasicMinoStyle extends MinoStyle {

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
    public void drawMino(Graphics2D g2D, int x, int y, int size, int color) {
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
            case GREY:
                g2D.setColor(flash);
                break;
            default:
                return;
        }

        g2D.fillRect(x, y, size, size);
    }

    /*@Override
    public void drawTetromino(Graphics2D g2D, int x_, int y_, int width,
            Tetromino t) {
        int[][] upState = t.getUpState();
        int currentMinoWidth = width / 4;
        
        boolean flag = false;
        for (int j = 0, y = y_; j < 4; j++) {
            for (int i = 0, x = x_; i < 4; i++, x += currentMinoWidth) {
                int color = upState[j][i];
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
                    default:
                        continue;
                }

                g2D.fillRect(x, y, currentMinoWidth, currentMinoWidth);
                flag = true;
            }
            
            if (flag) {
                y += currentMinoWidth;
            }
        }
    }*/

}
