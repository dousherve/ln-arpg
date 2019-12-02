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

        fullHearthRoi = new RegionOfInterest(32,0,16,16);
        halfHearthRoi = new RegionOfInterest(16,0,16,16);
        emptyHeartRoi = new RegionOfInterest(0,0,16,16);

        hearths = new ArrayList <ImageGraphics> ();

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

    public void updateHearts(float current_hp){
        RegionOfInterest roi;

        for (int i = 0; i < hearths.size(); ++i) {
            if(current_hp-i <= 0){
                roi = emptyHeartRoi;
            }else if(current_hp - i < 1){
                roi = halfHearthRoi;
            }else{
                roi = fullHearthRoi;
            }

            hearths.set(i, new ImageGraphics(ResourcePath.getSprite(hearthsDisplayImage),1.5f, 1.5f, roi));
            hearths.get(i).setDepth(DEPTH);

        }
    }

}
