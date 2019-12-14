package ch.epfl.cs107.play.game.arpg.actor.gui;

import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

interface ARPGStatusGUIElement {
    
    float DEPTH = Float.MAX_VALUE;
    float PADDING = 0.25f;
    
    /**
     * Draw the current GUI element on the canvas, with the given anchor.
     * @param canvas (Canvas) The canvas on which the current GUI element is being drawn
     * @param parentAnchor (Vector) The anchor of the GUI element
     */
    void draw(Canvas canvas, Vector parentAnchor);
    
}
