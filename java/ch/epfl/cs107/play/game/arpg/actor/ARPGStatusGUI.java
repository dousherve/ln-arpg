package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusGUIElement;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusHpGUI;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusItemGUI;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class ARPGStatusGUI implements Graphics {
    
    private static final float DEPTH = 100.f;
    
    private ARPGStatusItemGUI itemGUI;
    private ARPGStatusHpGUI hpGUI;
    
    private List<ARPGStatusGUIElement> guiElements;
    
    protected ARPGStatusGUI() {
        guiElements = new ArrayList<>();
        
        /// Current Item
        guiElements.add(itemGUI = new ARPGStatusItemGUI());
        
        // Health Points display
        // TODO: 02/12/2019 change magic number "5"
        guiElements.add(hpGUI = new ARPGStatusHpGUI(5));
    }
    
    @Override
    public void draw(Canvas canvas) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth() / 2, canvas.getScaledHeight() / 2));
        
        for (ARPGStatusGUIElement element : guiElements) {
            element.draw(canvas, anchor);
        }
    }
    
    /**
     * Update the GUI to display the Item which is currently held by the Player.
     * @param item (ARPGItem) The new Item to display
     */
    public void updateCurrentItem(ARPGItem item) {
        itemGUI.setItem(item);
    }
    
    /**
     * Update the GUI to show the correct heart display.
     * @param hp (float) The Health Points of the Player
     */
    public void updateHp(float hp) {
        hpGUI.updateHearts(hp);
    }
}
