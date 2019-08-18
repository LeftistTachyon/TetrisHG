package com.github.leftisttachyon.tetris.tetrominos.srs;

import com.github.leftisttachyon.tetris.ImageMinoStyle;

/**
 * The SRS style of minos
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class SRSMinoStyle extends ImageMinoStyle {
    
    /**
     * No instantiation for you!
     */
    private SRSMinoStyle() {
        super();
    }
    
    /**
     * Returns an instance of a SRSMinoStyle
     *
     * @return an instance of a SRSMinoStyle
     */
    public static SRSMinoStyle getMinoStyle() {
        return SINGLETON;
    }

    /**
     * The one and only.
     */
    private static final SRSMinoStyle SINGLETON = new SRSMinoStyle();

    @Override
    protected String getPath(int color) {
        switch (color) {
            case BLUE:
                return "/com/github/leftisttachyon/tetris/resources/srs/blue.png";
            case CYAN:
                return "/com/github/leftisttachyon/tetris/resources/srs/cyan.png";
            case GREEN:
                return "/com/github/leftisttachyon/tetris/resources/srs/green.png";
            case GREY:
                return "/com/github/leftisttachyon/tetris/resources/srs/lock.png";
            case ORANGE:
                return "/com/github/leftisttachyon/tetris/resources/srs/orange.png";
            case PURPLE:
                return "/com/github/leftisttachyon/tetris/resources/srs/purple.png";
            case RED:
                return "/com/github/leftisttachyon/tetris/resources/srs/red.png";
            case YELLOW:
                return "/com/github/leftisttachyon/tetris/resources/srs/yellow.png";
            default:
                return null;
        }
    }
}
