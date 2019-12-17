package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.gui.inventory.ARPGInventoryGUI;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Seller extends Character {

    private ARPGInventoryGUI inventoryGui;
    private ARPGPlayer player;
    private static ARPGItem[] ITEMS_IN_STOCK = new ARPGItem[]{
            ARPGItem.BOMB, ARPGItem.ARROW, ARPGItem.BOW
    };

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Seller(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        inventoryGui = new ARPGInventoryGUI(getOwnerArea().getCameraScaleFactor(), "Shop", ITEMS_IN_STOCK);
    }

    /**
     * Navigate through the inventory
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void navigateInventory(Orientation orientation, Button b) {
        if (b.isPressed()) {
            inventoryGui.navigate(orientation);
        }
    }

    /**
     * Handle the keyboard events
     */
    private void handleKeyboardEvents() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (state == State.SPECIAL) {
            navigateInventory(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            navigateInventory(Orientation.UP, keyboard.get(Keyboard.UP));
            navigateInventory(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            navigateInventory(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

            if (keyboard.get(Keyboard.ENTER).isPressed()) {
                ARPGItem item = inventoryGui.getSelectedItem();
                player.buyItem(item, item.getPrice(), 1);
            }
        }
    }

    public void personalInteraction(ARPGPlayer player) {
        this.player = player;
        state = state == State.SPECIAL ? State.IDLE : State.SPECIAL;
    }

    @Override
    public void update(float deltaTime) {
        inventoryGui.update(deltaTime);
        handleKeyboardEvents();
    }

    @Override
    public void draw(Canvas canvas) {
        movingAnimations[getOrientation().ordinal()].draw(canvas);

        if (state == State.SPECIAL) {
            inventoryGui.draw(canvas);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
