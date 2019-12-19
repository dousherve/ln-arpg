package ch.epfl.cs107.play.game.arpg.actor.gui.inventory;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.TextAlign;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ARPGInventoryGUI implements Graphics, Updatable {
    
    private static final String BACKGROUND_RES = "zelda/inventory.background";
    
    private static final float HEIGHT = 10f;
    private static final float BG_DEPTH = 200_000f;
    private static final float ELEMENT_DEPTH = 205_000f;

    private static final float TITLE_OFFSET = 1.75f;
    private static final float PADDING = .8f;
    private static final float SPACING = .5f;

    private ImageGraphics background;
    private TextGraphics title;

    private List<ARPGInventorySlotGUI> slots;
    private int currentItemIndex;
    
    public ARPGInventoryGUI(float width, String titleText) {
        RegionOfInterest roi = new RegionOfInterest(0, 0, 240, 240);

        title = new TextGraphics(titleText, 1f, Color.BLACK,
                null, 0,
                false, false, Vector.ZERO,
                TextAlign.Horizontal.CENTER, TextAlign.Vertical.BOTTOM, 1f, BG_DEPTH
        );
        title.setDepth(ELEMENT_DEPTH);

        background = new ImageGraphics(ResourcePath.getSprite(BACKGROUND_RES), width, HEIGHT,
                roi, Vector.ZERO, 1f, BG_DEPTH
        );
        
        slots = new ArrayList<>();
    }
    
    /**
     * Update the content of the current inventory GUI with the given items
     * @param items (Map<ARPGItem, Integer>) An HashMap containing the items needed and their quantities
     */
    public void updateContent(Map<ARPGItem, Integer> items){
        boolean exist = false;

        for (ARPGItem item : items.keySet()) {
            for (ARPGInventorySlotGUI slot : slots) {
                if (slot.getItem().getName().equals(item.getName())){
                    slot.setQuantity(items.get(item));
                    exist = true;
                }
            }
            if (!exist) {
                slots.add(new ARPGInventorySlotGUI(item, items.get(item), 2.5f, background.getAnchor()));
            }
        }
    }
    
    /**
     * @return (ARPGItem) The current selected Item.
     */
    public ARPGItem getSelectedItem() {
        return slots.get(currentItemIndex).getItem();
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < slots.size(); ++i) {
            final ARPGInventorySlotGUI slot = slots.get(i);
            slot.update(deltaTime);

            if (i == currentItemIndex) {
                slot.setSelected(true);
            } else {
                slot.setSelected(false);
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        final Vector bottomLeftAnchor = canvas.getTransform().getOrigin().sub(new Vector(canvas.getScaledWidth() / 2, canvas.getScaledHeight() / 2));

        title.setAnchor(bottomLeftAnchor.add(canvas.getScaledWidth() / 2, canvas.getScaledHeight() - TITLE_OFFSET));
        title.draw(canvas);

        Vector backgroundAnchor = new Vector(0, canvas.getScaledHeight() - HEIGHT);
        background.setAnchor(bottomLeftAnchor.add(backgroundAnchor));
        background.draw(canvas);

        final Vector topLeftAnchor = bottomLeftAnchor.add(PADDING, canvas.getScaledHeight() - TITLE_OFFSET - 3.5f);
        for (int i = 0; i < slots.size(); ++i) {
            final ARPGInventorySlotGUI slot = slots.get(i);
            slot.draw(canvas, topLeftAnchor.add((slot.getSize() + SPACING) * (i % 4), -(slot.getSize() + SPACING) * (i / 4)));
        }
    }
    
    /**
     * Move through the inventory, according to the given Orientation.
     * @param orientation (Orientation) The given orientation.
     */
    public void navigate(Orientation orientation) {
        switch (orientation) {
            case RIGHT:
                currentItemIndex = (currentItemIndex + 1) % slots.size();
                break;

            case LEFT:
                currentItemIndex = currentItemIndex - 1 < 0 ? slots.size() - 1 : currentItemIndex - 1;
                break;

            case DOWN:
                currentItemIndex += 4;
                if (currentItemIndex >= slots.size()) {
                    currentItemIndex = slots.size() - 1;
                }
                break;

            case UP:
                if (currentItemIndex - 4 >= 0) {
                    currentItemIndex -= 4;
                }
                break;

            default:
                break;
        }
    }

}
