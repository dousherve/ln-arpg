package ch.epfl.cs107.play.game.arpg.actor.gui.status;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 *  Graphic user interface element showing selected item.
 */
public class ARPGStatusItemGUI implements ARPGStatusGUIElement {

    private static final float SIZE = 1.75f;
    
    private ImageGraphics gear;
    private Sprite itemSprite;
    
    public ARPGStatusItemGUI() {
        gear = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"),
                SIZE, SIZE, new RegionOfInterest(0, 0, 32, 32));
        gear.setDepth(DEPTH);
    }
    
    public ARPGStatusItemGUI(ARPGItem item) {
        this();
        setItem(item);
    }
    
    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        Vector gearAnchor = new Vector(PADDING, canvas.getScaledHeight() - PADDING - SIZE);
        gear.setAnchor(parentAnchor.add(gearAnchor));
        gear.draw(canvas);
        
        if (itemSprite != null) {
            // Center the item in the gear
            Vector itemAnchor = gearAnchor.add(gear.getWidth() / 2f - itemSprite.getWidth() / 2f,
                    gear.getHeight() / 2f - itemSprite.getHeight() / 2f);
            itemSprite.setAnchor(parentAnchor.add(itemAnchor));
            itemSprite.draw(canvas);
        }
    }
    
    public void setItem(ARPGItem item) {
        if (item == null) {
            itemSprite = null;
            return;
        }
        
        this.itemSprite = item.getSprite();
        this.itemSprite.setDepth(DEPTH);
    }
    
    public Vector getRightAnchor() {
        return gear.getAnchor().add(gear.getWidth(), 0f);
    }
    
}
