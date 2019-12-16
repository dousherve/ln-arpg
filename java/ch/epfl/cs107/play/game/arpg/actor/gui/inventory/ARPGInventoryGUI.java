package ch.epfl.cs107.play.game.arpg.actor.gui.inventory;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;

public class ARPGInventoryGUI implements Graphics {
    
    private static final String BACKGROUND_RES = "zelda/inventory.background";
    
    private static final float HEIGHT = 10f;
    private static final float DEPTH = 10_000f;
    
    private ImageGraphics background;
    private ARPGInventorySlotGUI slot;
    
    public ARPGInventoryGUI(float width) {
        RegionOfInterest roi = new RegionOfInterest(0, 0, 240, 240);
        background = new ImageGraphics(ResourcePath.getSprite(BACKGROUND_RES), width, HEIGHT, 
                roi, new Vector(0, 0), 1f, DEPTH
        );
        slot = new ARPGInventorySlotGUI(ARPGItem.STAFF, 10, 2.5f, background.getAnchor());
    }
    
    @Override
    public void draw(Canvas canvas) {
        final Vector bottomLeftAnchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth() / 2, canvas.getScaledHeight() / 2));
        TextGraphics text = new TextGraphics("Inventory", 1f, Color.BLACK);
        text.setAnchor(bottomLeftAnchor);
        text.setDepth(15000f);
        text.draw(canvas);
        Vector backgroundAnchor = new Vector(0, canvas.getScaledHeight() - HEIGHT);
        background.setAnchor(bottomLeftAnchor.add(backgroundAnchor));
        background.draw(canvas);
        slot.draw(canvas, bottomLeftAnchor.add(backgroundAnchor));
    }
    
}
