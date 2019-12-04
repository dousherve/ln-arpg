package ch.epfl.cs107.play.game.arpg.actor.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;

public class ARPGStatusHpGUI implements ARPGStatusGUIElement {

    /// The resource name of the Heart sprite
    private static final String heartResourceName = "zelda/heartDisplay";
    
    /// The three different possible ROI for the heart Sprite 
    private static final RegionOfInterest FULL_HEART_ROI =
            new RegionOfInterest(32,0,16,16);
    private static final RegionOfInterest HALF_HEART_ROI =
            new RegionOfInterest(16,0,16,16);
    private static final RegionOfInterest EMPTY_HEART_ROI =
            new RegionOfInterest(0,0,16,16);
    
    /// The size of the hearts we want to display
    private static final float WIDTH = 1f, HEIGHT = 1f;
    
    private ImageGraphics[] hearts;
    
    /**
     * The default ARPGStatusHpGUI.
     * @param maxHp (int) An integer representing the maximum Health Points the player can have
     */
    public ARPGStatusHpGUI(int maxHp) {
        hearts = new ImageGraphics[maxHp];
        
        updateHearts(maxHp);
    }

    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        parentAnchor = parentAnchor.add(new Vector(2f,
                canvas.getScaledHeight() - 1.75f + HEIGHT / 4));
        Vector deltaAnchor = new Vector(1f, 0);

        for (int i = 0; i < hearts.length; ++i) {
            hearts[i].setAnchor(parentAnchor.add(deltaAnchor.mul((float) i)));
            hearts[i].draw(canvas);
        }
    }

    /**
     * Update the appearance of each heart of the HP GUI
     * @param hp (float) The HP of the player
     */
    public void updateHearts(float hp) {
        RegionOfInterest roi;

        // Set the appearance for each heart, depending of its position (i) and the current health of the player (hp)
        for (int i = 0; i < hearts.length; ++i) {
            if (hp - i <= 0) {
                roi = EMPTY_HEART_ROI;
            } else if(hp - i < 1){
                roi = HALF_HEART_ROI;
            } else{
                roi = FULL_HEART_ROI;
            }

            hearts[i] = new ImageGraphics(ResourcePath.getSprite(heartResourceName), WIDTH, HEIGHT, roi);
            hearts[i].setDepth(DEPTH);
        }
    }

}
