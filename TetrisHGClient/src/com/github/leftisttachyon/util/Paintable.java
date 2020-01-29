package com.github.leftisttachyon.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * An interface that represents something that can be painted with a
 * {@code paint} method.
 *
 * @author Jed Wang
 * @see Component#paint(java.awt.Graphics)
 * @since 0.9.0
 */
@FunctionalInterface
public interface Paintable {

    /**
     * Paints this Paintable object with the given {@code Graphics} object. The
     * painted output's top left corner is, by default, (0, 0).
     *
     * @param g the Graphics object to use to paint this component
     */
    default void paint(Graphics g) {
        paint((Graphics2D) g);
    }

    /**
     * Paints this Paintable object with the given {@code Graphics2D} object.
     * The painted output's top left corner is, by default, (0, 0).
     *
     * @param g2D the Graphics2D object to use to paint this component
     */
    void paint(Graphics2D g2D);

    /**
     * Paints this Paintable object with the given {@code Graphics2D} object.
     * The painted output's top left corner is (x, y).
     *
     * @param g2D the Graphics2D object to use to paint this component
     * @param x the x-coordinate of the top left corner
     * @param y the y-coordinate of the top left corner
     * @throws NoninvertibleTransformException thrown when the translation from
     * (0, 0) to (x, y) is noninvertible
     */
    default void paint(Graphics2D g2D, int x, int y)
            throws NoninvertibleTransformException {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        g2D.transform(at);

        paint(g2D);

        g2D.transform(at.createInverse());
    }

    /**
     * Paints this Paintable object with the given {@code Graphics} object. The
     * painted output's top left corner is (x, y).
     *
     * @param g the Graphics object to use to paint this component
     * @param x the x-coordinate of the top left corner
     * @param y the y-coordinate of the top left corner
     * @throws NoninvertibleTransformException thrown when the translation from
     * (0, 0) to (x, y) is noninvertible
     */
    default void paint(Graphics g, int x, int y)
            throws NoninvertibleTransformException {
        paint((Graphics2D) g, x, y);
    }
}
