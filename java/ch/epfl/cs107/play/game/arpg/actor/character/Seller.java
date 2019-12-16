package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.gui.inventory.ARPGInventoryGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Seller extends Character {

    private ARPGInventoryGUI inventoryGUI;

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Seller(Area area, Orientation orientation, DiscreteCoordinates position, ARPGItem ... itemToSell) {
        super(area, orientation, position);

        inventoryGUI = new ARPGInventoryGUI(getOwnerArea().getCameraScaleFactor(), ARPGItem.values());

    }

    @Override
    public void personalInteraction() {
        state = state == State.SPECIAL ? State.IDLE : State.SPECIAL;
    }

    @Override
    public void update(float deltaTime) {
        inventoryGUI.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        movingAnimations[getOrientation().ordinal()].draw(canvas);

        if (state == State.SPECIAL) {
            inventoryGUI.draw(canvas);
        }

    }



}
