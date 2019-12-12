package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.item.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.item.MagicWaterPojectile;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.item.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
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

    private enum State {
        IDLE, ATTACKING_WITH_BOW, ATTACKING_WITH_SWORD, ATTACKING_WITH_STAFF
    }
    
    private class ARPGPlayerHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }
        
        @Override
        public void interactWith(ARPGCollectableAreaEntity collectableEntity) {
            collectableEntity.setCollected();
        }
    
        @Override
        public void interactWith(Coin coin) {
            interactWith((ARPGCollectableAreaEntity) coin);
            collectItem(coin);
        }

        @Override
        public void interactWith(Heart heart) {
            interactWith((ARPGCollectableAreaEntity) heart);
            collectItem(heart);
        }
    
        @Override
        public void interactWith(CastleKey key) {
            interactWith((ARPGCollectableAreaEntity) key);
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

        @Override
        public void interactWith(Monster monster) {
            if (state == State.ATTACKING_WITH_SWORD) {
                monster.harm(Monster.Vulnerability.PHYSICAL, SWORD_DAMAGE);
            }
        }
        
    }
    
    /// The maximum Health Points of the player
    private static final float MAX_HP = 5f;
    /// Health points
    private float hp;

    /// Sword damage
    private static final float SWORD_DAMAGE = 2f;

    /// Attacking state of the player
    private State state;

    /// The timeout after which we can take damage again
    private final static float TIMEOUT_RECOVERY = .5f;
    /// The recovery timer
    private float recoveryTimer;
    
    /// The Inventory of the Player
    private final ARPGInventory inventory;
    private ARPGItem currentItem;
    private int currentItemIndex = -1;
    
    /// Keeps track whether we are displaying the fortune or the money
    private boolean isDisplayingMoney;

    // MARK:- Animations

    /// Animations arrays
    private Animation[] movingAnimations;
    private Animation[] swordAnimations;
    private Animation[] bowAnimations;
    private Animation[] staffAnimations;

    /// The current rendering animation
    private Animation currentAnimation;

    /// Index of the current animation in the above-mentioned arrays
    private int animationIndex;
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
        inventory.add(ARPGItem.BOW, 1);
        inventory.add(ARPGItem.ARROW, 100);
        
        setupAnimations();
        
        hp = MAX_HP;

        state = State.IDLE;
        
        isDisplayingMoney = true;
        recoveryTimer = 0f;
    
        resetMotion();
    }

    /**
     * Setup animations
     */
    private void setupAnimations() {
        // TODO: 12/12/2019 Can we optimize this?

        Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);

        Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, bowSprites,false);

        Sprite[][] swordSpites = RPGSprite.extractSprites("zelda/player.sword", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, swordSpites,false);

        Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        staffAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, staffSprites, false);


        animationIndex = getOrientation().ordinal();
    }

    private void updateCurrentAnimation(float deltaTime){
        currentAnimation.update(deltaTime);
        
        if (currentAnimation.isCompleted()) {
            currentAnimation.reset();
            state = State.IDLE;
        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        recoveryTimer = Math.max(recoveryTimer - deltaTime, 0);

        // Handle the keyboard events
        handleKeyboardEvents();
        
        if (currentItem == null || !possess(currentItem)) {
            switchCurrentItem();
        }
        
        // TODO: move that into a function only called when money or fortune changes when debug money is removed
        statusGui.updateMoney(isDisplayingMoney ? inventory.getMoney() : inventory.getFortune());
    
        // Update the animations
        
        animationIndex = getOrientation().ordinal();
        switch (state) {
            case IDLE:
                currentAnimation = movingAnimations[animationIndex];
                if (isDisplacementOccurs()) {
                    currentAnimation.update(deltaTime);
                } else {
                    currentAnimation.reset();
                }
                break;
        
            case ATTACKING_WITH_BOW:
                currentAnimation = bowAnimations[animationIndex];
                updateCurrentAnimation(deltaTime);
                break;
        
            case ATTACKING_WITH_SWORD:
                currentAnimation = swordAnimations[animationIndex];
                updateCurrentAnimation(deltaTime);
                break;
        
            case ATTACKING_WITH_STAFF:
                currentAnimation = staffAnimations[animationIndex];
                updateCurrentAnimation(deltaTime);
                break;
        
            default:
                break;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        // Animate the player
        currentAnimation.draw(canvas);

        // Draw the GUI
        statusGui.draw(canvas);
    }
    
    /**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, Button b) {
        if (b.isDown() && state == State.IDLE) {
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
            handleItemUsage();
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
        
        // TODO: remove debug heal
        if (keyboard.get(Keyboard.H).isPressed()) {
            heal(MAX_HP);
        }
    }
    
    /**
     * Switch between items by looping through the content of the Inventory
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
     * Handle the usage of the current held Item.
     */
    private void handleItemUsage() {
        if (currentItem == null) {
            // We return if the player doesn't currently hold any item
            return;
        }
        
        if (state == State.IDLE && !isDisplacementOccurs()) {
            
            switch (currentItem) {
                case BOMB:
                    if (useBomb())  inventory.remove(currentItem, 1);
                    break;
        
                case BOW:
                    state = State.ATTACKING_WITH_BOW;
                    
                    if (useBow()) inventory.remove(ARPGItem.ARROW, 1);
                    break;
        
                case SWORD:
                    state = State.ATTACKING_WITH_SWORD;
                    break;
        
                case STAFF:
                    state = State.ATTACKING_WITH_STAFF;
                    useStaff();
                    break;
        
                default:
                    break;
            }
            
        }
        System.out.println(state);
    }
    
    /**
     * Set a Bomb on the floor (if possible) and removes it from the inventory
     * @return (boolean) A boolean flag indicating if the Bomb has successfully been set
     */
    private boolean useBomb() {
        if (possess(ARPGItem.BOMB)) {
            DiscreteCoordinates bombPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            Bomb bomb = new Bomb(getOwnerArea(), bombPosition);
            
            if (canSummonEntity(bomb, bombPosition)) {
                getOwnerArea().registerActor(bomb);
                return true;
            }
        }
        return false;
    }

    /**
     * Shoot an Arrow (if possible) and removes it from the inventory
     * @return (boolean) A boolean flag indicating if the Arrow has successfully been shot
     */
    private boolean useBow() {
        if (possess(ARPGItem.ARROW)) {
            DiscreteCoordinates arrowPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            Arrow arrow = new Arrow(getOwnerArea(), getOrientation(), arrowPosition, 1, 3);

            if (canSummonEntity(arrow, arrowPosition)) {
                getOwnerArea().registerActor(arrow);
                return true;
            }
        }
        return false;
    }

    /**
     * Shoot a MagicWaterProjectile (if possible)
     * @return (boolean) A boolean flag indicating if the MagicWaterPojectile has successfully been shot
     */
    private void useStaff() {
        if (possess(ARPGItem.STAFF)) {
            DiscreteCoordinates pearlPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            MagicWaterPojectile pearl = new MagicWaterPojectile(getOwnerArea(), getOrientation(), pearlPosition, 1, 5);

            if(canSummonEntity(pearl, pearlPosition)) {
                getOwnerArea().registerActor(pearl);
            }
        }
    }
    
    /**
     * Use the Sword by notifying the Behavior that we want a view Interaction
     */
    private void useSword() {
        wantsViewInteraction();
    }

    /**
     *
     * @param entity (Interactable) the entity to summon
     * @param coords (List<DiscreteCoordinates>) the coordinates of the target cells
     * @return true if the entity can be summoned at the given position
     */
    private boolean canSummonEntity(AreaEntity entity, DiscreteCoordinates coords){
        return getOwnerArea().canEnterAreaCells(entity, Collections.singletonList(coords));
    }
    
    /**
     * Harm the player. Make sure that the HP level does not go below 0 or over MAX_HP.
     * @param hp (float) The amount of Health Points to remove from the ARPGPlayer
     */
    public void harm(float hp) {
        if (recoveryTimer > 0) {
            // If we're still recovering, don't take damage
            return;
        }
        
        this.hp = Math.min(Math.max(this.hp - hp, 0), MAX_HP);
        recoveryTimer = TIMEOUT_RECOVERY;
        statusGui.updateHp(this.hp);
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
