package com.github.leftisttachyon.tetris.resources.tgm;

import com.github.leftisttachyon.tetris.ImageMinoStyle;

/**
 * The TGM style of minos
 *
 * @author Jed Wang
 * @since 0.9.0
 */
public class TGMMinoStyle extends ImageMinoStyle {
    
    /**
     * No instantiation for you!
     */
    private TGMMinoStyle() {
        super();
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

    @Override
    protected String getPath(int color) {
        switch (color) {
            case BLUE:
                return "/com/github/leftisttachyon/tetris/resources/tgm/blue.png";
            case CYAN:
                return "/com/github/leftisttachyon/tetris/resources/tgm/cyan.png";
            case GREEN:
                return "/com/github/leftisttachyon/tetris/resources/tgm/green.png";
            case GREY:
                return "/com/github/leftisttachyon/tetris/resources/tgm/lock.png";
            case ORANGE:
                return "/com/github/leftisttachyon/tetris/resources/tgm/orange.png";
            case PURPLE:
                return "/com/github/leftisttachyon/tetris/resources/tgm/purple.png";
            case RED:
                return "/com/github/leftisttachyon/tetris/resources/tgm/red.png";
            case YELLOW:
                return "/com/github/leftisttachyon/tetris/resources/tgm/yellow.png";
            default:
                return null;
        }
    }
}
