package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.gui.inventory.ARPGInventoryGUI;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.HashMap;

public class Seller extends Character {

    private Sprite sprite;

    // MAK:- Shop
    private ARPGInventoryGUI inventoryGui;
    private ARPGPlayer player;

    private static HashMap<ARPGItem, Integer> ITEMS_IN_STOCK = new HashMap<>(){
        {put(ARPGItem.BOMB, 1);
         put(ARPGItem.ARROW, 1);
         put(ARPGItem.BOW, 1);
         put(ARPGItem.HEAL_POTION, 1);
        }
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

        sprite = new RPGSprite("zelda/seller", 1,2, this, new RegionOfInterest(0,0,16,32));
        inventoryGui = new ARPGInventoryGUI(getOwnerArea().getCameraScaleFactor(), "Shop");
        inventoryGui.updateContent(ITEMS_IN_STOCK);
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
        sprite.draw(canvas);

        if (state == State.SPECIAL) {
            inventoryGui.draw(canvas);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
