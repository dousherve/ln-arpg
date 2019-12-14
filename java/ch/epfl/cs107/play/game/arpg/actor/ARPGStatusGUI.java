package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusHpGUI;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusItemGUI;
import ch.epfl.cs107.play.game.arpg.actor.gui.ARPGStatusMoneyGUI;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGStatusGUI implements Graphics {
    
    private static final float DEPTH = 100.f;
    
    private final ARPGStatusItemGUI itemGUI;
    private final ARPGStatusHpGUI hpGUI;
    private final ARPGStatusMoneyGUI moneyGUI;
    
    protected ARPGStatusGUI(int maxHp) {
        
        // Current Item
        itemGUI = new ARPGStatusItemGUI();
        
        // Health Points display
        hpGUI = new ARPGStatusHpGUI(maxHp);
        
        /// Money display
        moneyGUI = new ARPGStatusMoneyGUI();
    }
    
    @Override
    public void draw(Canvas canvas) {
        Vector bottomLeftAnchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth() / 2, canvas.getScaledHeight() / 2));
        
        itemGUI.draw(canvas, bottomLeftAnchor);
        hpGUI.draw(canvas, itemGUI.getRightAnchor());
        
        moneyGUI.draw(canvas, bottomLeftAnchor);
    }
    
    /**
     * Update the GUI to display the Item which is currently held by the Player.
     * @param item (ARPGItem) The new Item to display
     */
    protected void updateCurrentItem(ARPGItem item) {
        itemGUI.setItem(item);
    }
    
    /**
     * Update the GUI to show the correct heart display.
     * @param hp (float) The Health Points of the Player
     */
    protected void updateHp(float hp) {
        hpGUI.updateHearts(hp);
    }
    
    /**
     * Update the money in the Money GUI
     * @param money (int) The new amount of money to display
     */
    protected void updateMoney(int money) {
        moneyGUI.setMoney(money);
    }
    
}
