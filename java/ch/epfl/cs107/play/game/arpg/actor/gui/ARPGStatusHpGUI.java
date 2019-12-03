package ch.epfl.cs107.play.game.arpg.actor.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;

public class ARPGStatusHpGUI implements ARPGStatusGUIElement {

    /// The resource name of the Heart
    private static final String heartResourceName = "zelda/heartDisplay";
    
    /// The three different possible ROI for the heart Sprite 
    private static final RegionOfInterest FULL_HEART_ROI = new RegionOfInterest(32,0,16,16);
    private static final RegionOfInterest HALF_HEART_ROI = new RegionOfInterest(16,0,16,16);
    private static final RegionOfInterest EMPTY_HEART_ROI = new RegionOfInterest(0,0,16,16);
    
    private ArrayList<ImageGraphics> hearts;

    public ARPGStatusHpGUI(float maxHp) {
        hearts = new ArrayList<>();

        for (int i = 0; i < maxHp; ++i) {
            hearts.add(i, new ImageGraphics(ResourcePath.getSprite(heartResourceName),1.5f, 1.5f, FULL_HEART_ROI));
        }

        updateHearts(maxHp);
    }

    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        parentAnchor = parentAnchor.add(new Vector(2f, canvas.getScaledHeight() - 1.75f));
        Vector deltaAnchor = new Vector(2f, 0);

        for (int i = 0; i < hearts.size(); ++i) {
            hearts.get(i).setAnchor(parentAnchor.add(deltaAnchor.mul((float) i)));
            hearts.get(i).draw(canvas);
        }
    }

    /**
     * Update the appearance of each heart of the HP GUI
     * @param hp (float) The HP of the player
     */
    public void updateHearts(float hp){
        RegionOfInterest roi;

        // Set the appearance for each heart, depending of its position (i) and the current health of the player (hp)
        for (int i = 0; i < hearts.size(); ++i) {
            if (hp-i <= 0) {
                roi = EMPTY_HEART_ROI;
            } else if(hp - i < 1){
                roi = HALF_HEART_ROI;
            } else{
                roi = FULL_HEART_ROI;
            }

            hearts.set(i, new ImageGraphics(ResourcePath.getSprite(heartResourceName),1.5f, 1.5f, roi));
            hearts.get(i).setDepth(DEPTH);
        }
    }

}
