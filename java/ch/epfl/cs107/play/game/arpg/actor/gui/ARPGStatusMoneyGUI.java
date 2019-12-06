package ch.epfl.cs107.play.game.arpg.actor.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGStatusMoneyGUI implements ARPGStatusGUIElement {
    
    private static final float WIDTH = 4f, HEIGHT = WIDTH / 2f;
    private static final float DIGITS_SIZE = .85f;
    
    private static final String COINS_RESOURCE_NAME = "zelda/coinsDisplay";
    private static final String DIGITS_RESOURCE_NAME = "zelda/digits";
    
    private ImageGraphics coinsDisplay;
    private ImageGraphics[] digits;
    private int money;
    
    public ARPGStatusMoneyGUI() {
        coinsDisplay = new ImageGraphics(ResourcePath.getSprite(COINS_RESOURCE_NAME),
                WIDTH, HEIGHT, new RegionOfInterest(0, 0, 64, 32));
        coinsDisplay.setDepth(DEPTH);
        
        digits = new ImageGraphics[3];
        
        money = 0;
    }
    
    @Override
    public void draw(Canvas canvas, Vector parentAnchor) {
        Vector coinsDisplayAnchor = new Vector(PADDING, PADDING);
        coinsDisplay.setAnchor(parentAnchor.add(coinsDisplayAnchor));
        coinsDisplay.draw(canvas);
    
        Vector digitAnchor = coinsDisplay.getAnchor().add((4f/11f) * WIDTH, 0f);
        final float SPACING = 0.75f;
        for (int i = 0; i < 3; ++i) {
            digits[i] = new ImageGraphics(ResourcePath.getSprite(DIGITS_RESOURCE_NAME),
                    DIGITS_SIZE, DIGITS_SIZE, getDigitRoi(i));
            digits[i].setAnchor(digitAnchor.add(i * SPACING, .65f));
            digits[i].setDepth(DEPTH);
            digits[i].draw(canvas);
        }
    }
    
    private RegionOfInterest getDigitRoi(int digit) {
        if (digit < 0 || digit > 9) {
            return null;
        }
        
        if (digit >= 1) {
            // 1...9
            int y = (digit <= 4) ? 0 : (digit <= 8) ? 16 : 32;
            return new RegionOfInterest((digit - 1) * 16, y, 16, 16);
        } else {
            // 0
            return new RegionOfInterest(16, 32, 16, 16);
        }
    }
    
    /**
     * Set the current amount of money to display on the screen.
     * @param money (int) The amount of money to display
     */
    public void setMoney(int money) {
        // Maximum 3 digits => 0 ... 999
        this.money = Math.max(0, Math.min(money, 999));
    }
    
    /**
     * Return the digit of a number at a certain position
     * @param number
     * @param pos (int)
     * @return (int) The requested digit
     */
    private int getDigit(int number, int pos) {
        int digit = 0;
        
        return digit;
    }
    
}
