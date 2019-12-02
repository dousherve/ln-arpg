package ch.epfl.cs107.play.game.arpg.actor.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;

public class ARPGStatusHpGUI implements ARPGStatusGUIElement {

    private String hearthsDisplayImage;
    private ArrayList<ImageGraphics> hearths;
    private RegionOfInterest fullHearthRoi;
    private RegionOfInterest halfHearthRoi;
    private RegionOfInterest emptyHeartRoi;


    public ARPGStatusHpGUI(float max_hp){
        hearthsDisplayImage = "zelda/heartDisplay";

        //three different stats of the hearts
        fullHearthRoi = new RegionOfInterest(32,0,16,16);
        halfHearthRoi = new RegionOfInterest(16,0,16,16);
        emptyHeartRoi = new RegionOfInterest(0,0,16,16);

        hearths = new ArrayList<>();

        for (int i = 0; i < max_hp; ++i) {
            hearths.add(i, new ImageGraphics(ResourcePath.getSprite(hearthsDisplayImage),1.5f, 1.5f, fullHearthRoi));
        }

        updateHearts(max_hp);
    }

    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        parentAnchor = parentAnchor.add(new Vector(2f, canvas.getScaledHeight()-1.75f));
        Vector deltaAnchor = new Vector(2f, 0);

        for (int i = 0; i < hearths.size(); ++i) {
            hearths.get(i).setAnchor(parentAnchor.add(deltaAnchor.mul((float) i)));
            hearths.get(i).draw(canvas);
        }

    }

    /**
     * update appearance of each heart of the hp GUI
     * @param currentHp (float) current health of the player
     */
    public void updateHearts(float currentHp){
        RegionOfInterest roi;

        //set the appearance for each heart, depending of his position (i) and the current health of the player (currentHp)
        for (int i = 0; i < hearths.size(); ++i) {
            if(currentHp-i <= 0){
                roi = emptyHeartRoi;
            }else if(currentHp - i < 1){
                roi = halfHearthRoi;
            }else{
                roi = fullHearthRoi;
            }

            hearths.set(i, new ImageGraphics(ResourcePath.getSprite(hearthsDisplayImage),1.5f, 1.5f, roi));
            hearths.get(i).setDepth(DEPTH);

        }
    }

}
