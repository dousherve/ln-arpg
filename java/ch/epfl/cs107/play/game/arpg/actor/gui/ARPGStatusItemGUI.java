package ch.epfl.cs107.play.game.arpg.actor.gui;


import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGStatusItemGUI implements ARPGStatusGUIElement {
    
    private ImageGraphics gear;
    private Sprite itemSprite;
    
    public ARPGStatusItemGUI() {
        gear = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"),
                1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32));
        gear.setDepth(DEPTH);
    }
    
    public ARPGStatusItemGUI(ARPGItem item) {
        this();
        setItem(item);
    }
    
    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        Vector anchor = new Vector(0.25f, canvas.getScaledHeight() - 1.75f);
        gear.setAnchor(parentAnchor.add(anchor));
        gear.draw(canvas);
        if (itemSprite != null) {
            // Center the item in the gear
            Vector itemAnchor = anchor.add(gear.getWidth() / 2 - itemSprite.getWidth() / 2,
                    gear.getHeight() / 2 - itemSprite.getHeight() / 2);
            itemSprite.setAnchor(parentAnchor.add(itemAnchor));
            itemSprite.draw(canvas);
        }
    }
    
    public void setItem(ARPGItem item) {
        this.itemSprite = item.getSprite();
        itemSprite.setDepth(Float.MAX_VALUE);
    }
    
}
