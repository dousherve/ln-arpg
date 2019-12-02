package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusHpGUI;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusItemGUI;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGStatusGUI implements Graphics {
    
    private static final float DEPTH = 100.f;
    
    private ARPGStatusItemGUI itemGui;
    private ARPGStatusHpGUI hpGUI;
    
    protected ARPGStatusGUI() {
        itemGui = new ARPGStatusItemGUI();
        // TODO: 02/12/2019 change magic number "5"
        hpGUI = new ARPGStatusHpGUI(5);
    }
    
    @Override
    public void draw(Canvas canvas) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth() / 2, canvas.getScaledHeight() / 2));
        
        itemGui.draw(canvas, anchor);
        hpGUI.draw(canvas, anchor);
    }
    
    /**
     * Update the GUI to display the new Item.
     * @param item (ARPGItem) The new Item to display
     */
    public void updateItem(ARPGItem item) {
        itemGui.setItem(item);
    }

    public void updateHp(float currenHp) {
        hpGUI.updateHearts(currenHp);
    }
}
