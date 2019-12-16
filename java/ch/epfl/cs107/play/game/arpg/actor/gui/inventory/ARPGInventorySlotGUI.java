package ch.epfl.cs107.play.game.arpg.actor.gui.inventory;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.TextAlign;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;

public class ARPGInventorySlotGUI implements Updatable {
    
    private static final float DEPTH = 205_000f;
    
    private static final String SLOT_RES = "zelda/inventory.slot";
    private static final String SELECTOR_RES = "zelda/inventory.selector";
    
    /// The item to display
    private ARPGItem item;
    /// The sprite of the item
    private Sprite itemSprite;
    /// The quantity of the item to display
    private int quantity;
    /// Keep track if the current slot is selected or not
    private boolean isSelected;
    
    /// Flag used for blinking
    private boolean blinkingFlag;
    /// Used for blinking timing
    private float blinkingTimer;
    /// Blinking cycle delay
    private static final float BLINKING_DELAY = .25f;
    
    /// Regions of interest
    private final RegionOfInterest UNSELECTED_ROI;
    // 2 ROIs for blinking when selected
    private final RegionOfInterest SELECTED_ROI_1;
    private final RegionOfInterest SELECTED_ROI_2;
    
    /// The size of the slot
    private final float size;
    /// The anchor
    private final Vector anchor;
    /// The frame of the current slot: depends on the current state of the item
    private ImageGraphics frameGraphics;
    /// The text indicating the quantity of the current item
    private TextGraphics quantityText;
    
    protected ARPGInventorySlotGUI(ARPGItem item, int quantity, float size, Vector anchor) {
        this.item = item;
        this.itemSprite = new RPGSprite(
                "zelda/" + item.getResourceName(),
                0.75f, 0.75f,
                null, new RegionOfInterest(0, 0, 16, 16)
        );
        this.quantity = quantity;
        this.anchor = anchor;
        this.size = size;
        
        UNSELECTED_ROI = new RegionOfInterest(0, 0, 64, 64);
        SELECTED_ROI_1 = new RegionOfInterest(0, 0, 64, 64);
        SELECTED_ROI_2 = new RegionOfInterest(64, 0, 64, 64);
        
        isSelected = false;
        blinkingFlag = false;
        
        quantityText = new TextGraphics(
                "", .25f, Color.BLACK, null, 0f,
                false, false, Vector.ZERO,
                TextAlign.Horizontal.CENTER, TextAlign.Vertical.BOTTOM,
                1f, DEPTH
        );
        updateGui();
    }
    
    @Override
    public void update(float deltaTime) {
        blinkingTimer += deltaTime;
        if (blinkingTimer >= BLINKING_DELAY) {
            blinkingFlag = !blinkingFlag;
            blinkingTimer = 0f;
            updateGui();
        }
    }
    
    /**
     * Draw the current Slot on a given canvas, with a specific parent anchor.
     * 
     * @param canvas (Canvas) The canvas on which the slot will be drawn
     * @param parentAnchor (Vector) The parent anchor
     */
    public void draw(Canvas canvas, Vector parentAnchor) {
        // Draw the frame, depending on whether the item is selected or not
        frameGraphics.setAnchor(parentAnchor);
        frameGraphics.draw(canvas);
        
        // Draw the item
        itemSprite.setAnchor(frameGraphics.getAnchor().add(
                (size - item.getSprite().getWidth()) / 2,
                (size - item.getSprite().getHeight()) / 2
        ));
        itemSprite.setDepth(DEPTH);
        itemSprite.draw(canvas);

        // Draw the quantity
        quantityText.setAnchor(parentAnchor.add(size / 2, .4f));
        quantityText.draw(canvas);
    }
    
    /**
     * Update the GUI with the current data.
     */
    private void updateGui() {
        RegionOfInterest frameRoi;
        if (isSelected) {
            frameRoi = blinkingFlag ? SELECTED_ROI_1 : SELECTED_ROI_2;
        } else {
            frameRoi = UNSELECTED_ROI;
        }
        
        frameGraphics = new ImageGraphics(
                ResourcePath.getSprite(isSelected ? SELECTOR_RES : SLOT_RES),
                size, size, frameRoi,
                anchor, 1f, DEPTH
        );
    
        quantityText.setText("x" + quantity);
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateGui();
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        updateGui();
    }

    public float getSize() {
        return size;
    }

}
