package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.item.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
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
        
        public void interactWith(ARPGCollectableAreaEntity collectableEntity) {
            collectableEntity.setCollected();
        }
    
        @Override
        public void interactWith(Coin coin) {
            coin.setCollected();
            collectItem(coin);
        }

        @Override
        public void interactWith(Heart heart) {
            heart.setCollected();
            collectItem(heart);
        }
    
        @Override
        public void interactWith(CastleKey key) {
            key.setCollected();
            collectItem(key);
        }
    
        @Override
        public void interactWith(CastleDoor door) {
            if (door.isOpen()) {
                setIsPassingADoor(door);
                door.close();
            } else if (possess(ARPGItem.CASTLE_KEY)) {
                door.open();
            }
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
    
    /// Keeps track if we are displaying the fortune or the money
    private boolean isDisplayingMoney;
    
    /// Animations array
    private Animation[] playerAnimations;
    /// Index of the current animation in the above-mentioned array
    private int playerAnimationIndex;
    /// Animation duration in number of frames
    private static final int ANIMATION_DURATION = 2;
    
    /// InteractionVisitor handler
    private final ARPGPlayerHandler handler;
    
    /// The status GUI
    private ARPGStatusGUI statusGui;
    
    /**
     * Default ARPGPlayer constructor
     * @param owner (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        
        statusGui = new ARPGStatusGUI();
        
        handler = new ARPGPlayerHandler();
        inventory = new ARPGInventory(165);
    
        // TODO: remove debug default inventory items
        inventory.add(ARPGItem.BOMB, 5);
        
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        playerAnimations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);
        playerAnimationIndex = getOrientation().ordinal();
        
        hp = MAX_HP;
        
        isDisplayingMoney = true;
    
        resetMotion();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        playerAnimationIndex = getOrientation().ordinal();
        if (isDisplacementOccurs()) {
            playerAnimations[playerAnimationIndex].update(deltaTime);
        } else {
            playerAnimations[playerAnimationIndex].reset();
        }
        
        handleKeyboardEvents();
        
        if (currentItem == null || !possess(currentItem)) {
            switchCurrentItem();
        }
        
        // TODO: move that into a function only called when money or fortune changes when debug money is removed
        statusGui.updateMoney(isDisplayingMoney ? inventory.getMoney() : inventory.getFortune());
    }
    
    @Override
    public void draw(Canvas canvas) {
        // Animate the player
        playerAnimations[playerAnimationIndex].draw(canvas);

        // Draw the GUI
        statusGui.draw(canvas);
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
        
        if (keyboard.get(Keyboard.F).isPressed()) {
            // Switch between fortune and money display by pressing 'F'
            isDisplayingMoney = !isDisplayingMoney;
        }
        
        // TODO: remove debug stuff
        if (keyboard.get(Keyboard.Z).isPressed()) {
            for (ARPGItem item : ARPGItem.values()) {
                inventory.add(item, 5);
            }
        }
        
        // TODO: remove debug money
        if (keyboard.get(Keyboard.M).isPressed()) {
            inventory.addMoney(10);
        }
        
        // TODO: remove debug display pos
        if (keyboard.get(Keyboard.P).isPressed()) {
            System.out.println(getCurrentMainCellCoordinates().toString());
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
            ARPGItem item = inventory.getItems()[currentItemIndex];
            if (item == currentItem) {
                // We try again if we find an item which is different from the current one
                // We have to do this beacause the swicthing is not done properly
                // if we just picked up an Item
                currentItemIndex = (currentItemIndex + 1) % inventory.getItems().length;
                item = inventory.getItems()[currentItemIndex];
            }
            currentItem = item;

            // TODO: Remove debug sysout
            System.out.println("Current item: " + currentItem.getName() +
                    " (" + inventory.getQuantity(currentItem) + ")");
        } else {
            currentItem = null;
        }
    
        // Update the GUI
        statusGui.updateCurrentItem(currentItem);
    }
    
    // MARK:- Specific ARPGPlayer methods
    
    
    
    // MARK:- Item collect
    
    /**
     * Handle coin collection
     * @param coin (Coin) A coin
     */
    private void collectItem(Coin coin){
        inventory.addMoney(coin.getValue());
    }
    
    /**
     * Handle heart collection
     * @param heart (Heart) A heart
     */
    private void collectItem(Heart heart) {
        heal(heart.getHp());
    }
    
    /**
     * Handle CastleKey collection
     * @param key (CastleKey) A CastleKey
     */
    private void collectItem(CastleKey key) {
        inventory.add(ARPGItem.CASTLE_KEY, 1);
    }
    
    // MARK:- Handle items usage
    
    /**
     * Set a Bomb on the floor (if possible) and removes it from the inventory
     * @return (boolean) A boolean flag indicating if the Bomb has successfully been set
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
    
    /**
     * Harm the player. Make sure that the HP level does not go below 0 or over MAX_HP.
     * @param hp (float) The amount of Health Points to remove from the ARPGPlayer
     */
    public void harm(float hp) {
        this.hp = Math.min(Math.max(this.hp - hp, 0), MAX_HP);
        statusGui.updateHp(this.hp);
        // TODO: remove debug sysout
        System.out.println("HP (" + this.hp + ")");
    }

    /**
     * Heal the player.
     * @param hp (float) The amount of Health Points to add from the ARPGPlayer
     */
    public void heal(float hp) {
        harm(-hp);
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
        // The ARPGPlayer wants view interaction if the 'E' key is pressed
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
