package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ARPGPlayer extends Player implements Inventory.Holder {
    
    private class ARPGPlayerHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }
    
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }

    }
    
    /// The maximum Health Points of the player
    private static final float MAX_HP = 5.f;
    /// Health points
    private float hp;
    
    /// The Inventory of the Player
    private final ARPGInventory inventory;
    private ARPGItem currentItem;
    private int currentItemIndex = -1;
    
    /// Animations array
    private Animation[] animations;
    /// Index of the current animation in the above-mentioned array
    private int animationIndex;
    /// Animation duration in number of frames
    private static final int ANIMATION_DURATION = 2;
    
    /// InteractionVisitor handler
    private final ARPGPlayerHandler handler;
    
    /**
     * Default ARPGPlayer constructor
     * @param owner (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        
        handler = new ARPGPlayerHandler();
        inventory = new ARPGInventory(5000);
        
        inventory.add(ARPGItem.BOMB, 3);
        /*inventory.add(ARPGItem.STAFF, 1);
        inventory.add(ARPGItem.CASTLE_KEY, 1);*/
        
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        animations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);
        animationIndex = getOrientation().ordinal();
        
        hp = MAX_HP;
    
        resetMotion();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    
        animationIndex = getOrientation().ordinal();
        if (isDisplacementOccurs()) {
            animations[animationIndex].update(deltaTime);
        } else {
            animations[animationIndex].reset();
        }
        
        handleKeyboardEvents();
        
        if (currentItem == null) {
            switchCurrentItem();
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        animations[animationIndex].draw(canvas);
    }
    
    /**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (getOrientation() == orientation) move(ANIMATION_DURATION);
            else orientate(orientation);
        }
    }
    
    /**
     * Handle the keyboard events
     */
    private void handleKeyboardEvents() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        
        moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
        moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        
        if (keyboard.get(Keyboard.TAB).isPressed()) {
            switchCurrentItem();
        }
        
        if (keyboard.get(Keyboard.SPACE).isPressed()) {
            handleItemUse();
        }
    }
    
    private void handleItemUse() {
        if (currentItem == null) {
            return;
        }
        
        switch (currentItem) {
            case BOMB:
                if (useBomb())  inventory.remove(currentItem, 1);
                break;
            default:
                break;
        }
    }
    
    /**
     * Switch between items by looping through the Inventory
     */
    private void switchCurrentItem() {
        if (!inventory.isEmpty()) {
        
            if (currentItemIndex == -1) {
                currentItemIndex = 0;
            }
            
            currentItemIndex = (currentItemIndex + 1) % inventory.getItems().length;
            currentItem = (ARPGItem) inventory.getItems()[currentItemIndex];
    
            // TODO: Remove debug sysout
            System.out.println("Current item: " + currentItem.getName() +
                    " (" + inventory.getQuantity(currentItem) + ")");
        }
    }
    
    // MARK:- Handle items usage
    
    /**
     * Put a Bomb on the floor if possible and removes it from the inve
     */
    private boolean useBomb() {
        if (possess(ARPGItem.BOMB)) {
            DiscreteCoordinates bombPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            Bomb bomb = new Bomb(getOwnerArea(), bombPosition);
            
            if (getOwnerArea().canEnterAreaCells(bomb, Collections.singletonList(bombPosition))) {
                getOwnerArea().registerActor(bomb);
                return true;
            }
        }
        
        return false;
    }

    public void harm(float hp) {
        this.hp = Math.max(this.hp - hp, 0);
        // TODO: remove debug sysout
        System.out.println("Damage (" + this.hp + ")");
    }
    
    // MARK:- Inventory.Holder
    @Override
    public boolean possess(InventoryItem item) {
        return inventory.contains(item);
    }
    
    // MARK:- Interactable, Interactor
    
    @Override
    public boolean takeCellSpace() {
        return true;
    }
    
    @Override
    public boolean isCellInteractable() {
        return true;
    }
    
    @Override
    public boolean isViewInteractable() {
        return true;
    }
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }
    
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return getOwnerArea().getKeyboard().get(Keyboard.E).isPressed();
    }
    
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
