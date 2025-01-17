package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.character.Alice;
import ch.epfl.cs107.play.game.arpg.actor.character.Character;
import ch.epfl.cs107.play.game.arpg.actor.character.Seller;
import ch.epfl.cs107.play.game.arpg.actor.gui.inventory.ARPGInventoryGUI;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Staff;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Sword;
import ch.epfl.cs107.play.game.arpg.actor.item.projectile.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.item.projectile.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.actor.terrain.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Chest;
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
import java.util.List;

public class ARPGPlayer extends Player implements Inventory.Holder {

    private enum State {
        IDLE, SHOPPING, CHATTING, INVENTORY,
        ATTACKING_WITH_BOW, ATTACKING_WITH_SWORD, ATTACKING_WITH_STAFF
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
        public void interactWith(Staff staff) {
            interactWith((ARPGCollectableAreaEntity) staff);
            collectItem(staff);
        }

        @Override
        public void interactWith(CastleKey key) {
            interactWith((ARPGCollectableAreaEntity) key);
            collectItem(key);
        }

        @Override
        public void interactWith(Sword sword) {
            interactWith((ARPGCollectableAreaEntity) sword);
            collectItem(sword);
        }

        @Override
        public void interactWith(CastleDoor door) {
            if (state == State.IDLE) {
                if (door.isOpen()) {
                    setIsPassingADoor(door);
                    door.close();
                } else if (possess(ARPGItem.CASTLE_KEY)) {
                    door.open();
                }
            }
        }

        @Override
        public void interactWith(Chest chest) {
            chest.open();
        }

        @Override
        public void interactWith(Character character) {
            if (shouldSlay() && !character.isInvincible()) {
                character.harm(SWORD_DAMAGE);
            } else if (state == State.IDLE || state == State.CHATTING) {
                state = (state == State.IDLE) ? State.CHATTING : State.IDLE;
                character.personalInteraction();
            }
        }

        @Override
        public void interactWith(Alice alice) {
            if (state == State.IDLE || state == State.CHATTING) {
                if (possess(ARPGItem.SWORD)) {
                    alice.beginQuest();
                } else {
                    alice.personalInteraction();
                }
                
                state = (state == State.IDLE) ? State.CHATTING : State.IDLE;
            }
        }

        @Override
        public void interactWith(Seller seller) {
            if (state == State.IDLE || state == State.SHOPPING) {
                seller.personalInteraction(ARPGPlayer.this);
                state = (state == State.IDLE) ? State.SHOPPING : State.IDLE;
            }
        }

        // Sword interactions

        @Override
        public void interactWith(Monster monster) {
            if (shouldSlay()) {
                monster.harm(Monster.Vulnerability.PHYSICAL, SWORD_DAMAGE);
            }
        }
    
        @Override
        public void interactWith(Grass grass) {
            if (shouldSlay()) {
                grass.cut();
            }
        }
    
        @Override
        public void interactWith(Bomb bomb) {
            if (shouldSlay()) {
                bomb.explode();
            }
        }
        
    }
    
    /// The maximum Health Points of the player
    private static final float MAX_HP = 5f;
    /// Health points
    private float hp;

    /// Sword damage
    private static final float SWORD_DAMAGE = 2f;
    /// The default Projectile speed
    private static final float PROJECTILE_SPEED = 5;
    /// The default maximum distance an Arrow can travel
    private static final float MAX_ARROW_DISTANCE = 3;
    /// The default maximum distance a MagicWaterProjectile can travel
    private static final float MAX_WATER_PROJECTILE_DISTANCE = 5;

    /// State of the player
    private State state;

    /// The Inventory of the Player
    private final ARPGInventory inventory;
    private ARPGItem currentItem;
    private int currentItemIndex = -1;
    
    /// Keep track whether we are displaying the fortune or the money
    private boolean isDisplayingMoney;

    // MARK:- Animations

    /// Animations arrays
    private Animation[] movingAnimations;
    private Animation[] swordAnimations;
    private Animation[] bowAnimations;
    private Animation[] staffAnimations;

    /// The current rendering animation
    private Animation currentAnimation;

    /// Animation duration in number of frames
    private static final int MOVING_ANIMATION_DURATION = 4;
    /// Action animation duration in number of frames
    private static final int ACTION_ANIMATION_DURATION = 1;
    
    /// The amount of HP the Health Potion gives
    private static final float HEALTH_POTION_AMOUNT = 2.5f;

    /// InteractionVisitor handler
    private final ARPGPlayerHandler handler;
    
    /// The status GUI
    private final ARPGStatusGUI statusGui;
    
    /// The Inventory GUI
    private final ARPGInventoryGUI inventoryGui;
    private boolean isDisplayingInventory;
    
    /**
     * Default ARPGPlayer constructor
     * @param owner (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        
        statusGui = new ARPGStatusGUI((int) MAX_HP);
        
        handler = new ARPGPlayerHandler();
        inventory = new ARPGInventory(30);

        // We set the width of the Inventory GUI to the scale factor of the Area
        inventoryGui = new ARPGInventoryGUI(getOwnerArea().getCameraScaleFactor(), "Inventory");
        
        setupAnimations();
        
        hp = MAX_HP;

        state = State.IDLE;
        
        isDisplayingMoney = true;
        isDisplayingInventory = false;

        resetMotion();
    }

    /**
     * Setup animations
     */
    private void setupAnimations() {
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION / 2, sprites);

        Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        bowAnimations = RPGSprite.createAnimations(ACTION_ANIMATION_DURATION, bowSprites,false);

        Sprite[][] swordSpites = RPGSprite.extractSprites("zelda/player.sword", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        swordAnimations = RPGSprite.createAnimations(ACTION_ANIMATION_DURATION, swordSpites,false);

        Sprite[][] staffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4,
                2, 2, this, 32, 32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        staffAnimations = RPGSprite.createAnimations(ACTION_ANIMATION_DURATION, staffSprites, false);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (hasBeenHurt) {
            blink(deltaTime);
        }

        // Handle the keyboard events
        handleKeyboardEvents();

        if (currentItem == null || !possess(currentItem)) {
            switchCurrentItem();
        }
    
        // Update the amount of money displayed on the GUI
        statusGui.updateMoney(isDisplayingMoney ? inventory.getMoney() : inventory.getFortune());

        // Update the inventory
        inventoryGui.update(deltaTime);
        
        // Behave according to the current state
        
        switch (state) {
            case ATTACKING_WITH_BOW:
                if (currentAnimation.isCompleted()) {
                    // We try to shoot the arrow if the bending animation is done
                    if (useBow()) {
                        inventory.remove(ARPGItem.ARROW, 1);
                    }
        
                    state = State.IDLE;
                }
                break;
        
            case ATTACKING_WITH_STAFF:
                if (currentAnimation.isCompleted()) {
                    useStaff();
                    state = State.IDLE;
                }
                break;
        
            default:
                break;
        }
        
        // Update the current animation 
        // (we write this code in a second switch statement
        // and not in the preivous for clarity's sake only)
        
        final int ANIMATION_INDEX = getOrientation().ordinal();
        
        switch (state) {
            case ATTACKING_WITH_BOW:
                currentAnimation = bowAnimations[ANIMATION_INDEX];
                break;
        
            case ATTACKING_WITH_SWORD:
                currentAnimation = swordAnimations[ANIMATION_INDEX];
                if (currentAnimation.isCompleted()) {
                    state = State.IDLE;
                }
                break;
        
            case ATTACKING_WITH_STAFF:
                currentAnimation = staffAnimations[ANIMATION_INDEX];
                break;
    
            case IDLE:
                // We reset the action animations
                for (int i = 0; i < Orientation.values().length; ++i) {
                    swordAnimations[i].reset();
                    bowAnimations[i].reset();
                    staffAnimations[i].reset();
                }
                
                currentAnimation = movingAnimations[ANIMATION_INDEX];
                if (isDisplacementOccurs()) {
                    currentAnimation.update(deltaTime);
                } else {
                    currentAnimation.reset();
                }
                // We return here, because it is the only animation
                // which may not be updated afterwards
                return;
        
            default:
                return;
        }
        
        // Update the current animation, only executed if not IDLE
        currentAnimation.update(deltaTime);
    }


    @Override
    public void draw(Canvas canvas) {
        // Animate the player
        if (isVisible()) {
            currentAnimation.draw(canvas);
        }
        
        // Draw the status GUI
        statusGui.draw(canvas);

        if (isDisplayingInventory) {
            /// Draw the inventory GUI
            inventoryGui.draw(canvas);
        }
    }
    
    /**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (getOrientation() == orientation) move(MOVING_ANIMATION_DURATION);
            else orientate(orientation);
        }
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
        
        if (state == State.IDLE) {
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
        } else if (state == State.INVENTORY) {
            navigateInventory(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            navigateInventory(Orientation.UP, keyboard.get(Keyboard.UP));
            navigateInventory(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            navigateInventory(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        // Toggle the inventory GUI
        if (keyboard.get(Keyboard.I).isPressed() && (state == State.IDLE || state == State.INVENTORY)) {
            inventoryGui.updateContent(inventory.getItemsAndQuantity());
            isDisplayingInventory = !isDisplayingInventory;
            state = isDisplayingInventory ? State.INVENTORY : State.IDLE;
        }
        
        if (keyboard.get(Keyboard.F).isPressed()) {
            // Switch between fortune and money display by pressing 'F'
            isDisplayingMoney = !isDisplayingMoney;
        }
        
        // DEBUG:- Give a quantity of 5 for each item
        if (keyboard.get(Keyboard.Z).isPressed()) {
            for (ARPGItem item : ARPGItem.values()) {
                inventory.add(item, 5);
            }
        }
        
        // DEBUG:- Add 10 coins to the player's money
        if (keyboard.get(Keyboard.M).isPressed()) {
            inventory.addMoney(10);
        }
        
        // DEBUG:- Print the current coordinates
        if (keyboard.get(Keyboard.P).isPressed()) {
            System.out.println(getCurrentMainCellCoordinates().toString());
        }
        
        // DEBUG:- Fully heal the player
        if (keyboard.get(Keyboard.H).isPressed()) {
            heal(MAX_HP);
        }
    }
    
    /**
     * Switch between items by looping through the content of the current Inventory
     */
    private void switchCurrentItem() {
        if (state != State.IDLE) {
            return;
        }
        
        if (!inventory.isEmpty()) {
        
            if (currentItemIndex == -1) {
                currentItemIndex = 0;
            }
    
            currentItemIndex = (currentItemIndex + 1) % inventory.getItems().length;
            ARPGItem item = inventory.getItems()[currentItemIndex];
            if (item == currentItem) {
                // We try again if we find an item which is different from the current one
                // We have to do this because the switching is not done properly
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
    
    /**
     * @return (boolean) a boolean indicating if the Player can slay.
     */
    private boolean shouldSlay() {
        return state == State.ATTACKING_WITH_SWORD && currentAnimation.isCompleted();
    }
    
    // MARK:- Collect the items
    
    /**
     * Handle coin collection
     * @param coin (Coin) A coin
     */
    private void collectItem(Coin coin) {
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

    /**
     * Handle Staff collection
     * @param staff (Staff) A Staff
     */
    private void collectItem(Staff staff) {
        inventory.add(ARPGItem.STAFF, 1);
    }

    /**
     * Handle Sword collection
     * @param sword (Sword) A Sword
     */
    private void collectItem(Sword sword) { inventory.add(ARPGItem.SWORD, 1); }


    /**
     * Buy an item from a Seller
     * @param item  (ARPGItem) the item to buy
     * @param price (int) price of the item
     * @param quantity (int) quantity of the item
     */
    public void buyItem(ARPGItem item, int price, int quantity) {
        if (inventory.getMoney() >= price) {
            inventory.removeMoney(price);
            inventory.add(item, quantity);
        }
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
                    break;
        
                case SWORD:
                    state = State.ATTACKING_WITH_SWORD;
                    break;

                case STAFF:
                    state = State.ATTACKING_WITH_STAFF;
                    break;

                case HEAL_POTION:
                    heal(HEALTH_POTION_AMOUNT);
                    inventory.remove(ARPGItem.HEAL_POTION, 1);
                    break;
        
                default:
                    break;
            }
            
        }
    }
    
    /**
     * Set a Bomb on the floor (if possible) and removes it from the inventory
     * @return (boolean) A boolean flag indicating if the Bomb has successfully been set
     */
    private boolean useBomb() {
        if (possess(ARPGItem.BOMB)) {
            DiscreteCoordinates bombPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            Bomb bomb = new Bomb(getOwnerArea(), bombPosition);
            
            if (canSpawnEntity(bomb, bombPosition)) {
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
            Arrow arrow = new Arrow(
                    getOwnerArea(), getOrientation(), arrowPosition, 
                    PROJECTILE_SPEED, MAX_ARROW_DISTANCE
            );

            if (canSpawnEntity(arrow, arrowPosition)) {
                getOwnerArea().registerActor(arrow);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Shoot a MagicWaterProjectile (if possible)
     */
    private void useStaff() {
        if (possess(ARPGItem.STAFF)) {
            DiscreteCoordinates projectilePos = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
            MagicWaterProjectile projectile = new MagicWaterProjectile(
                    getOwnerArea(), getOrientation(), 
                    projectilePos, PROJECTILE_SPEED, MAX_WATER_PROJECTILE_DISTANCE
            );

            if (canSpawnEntity(projectile, projectilePos)) {
                getOwnerArea().registerActor(projectile);
            }
        }
    }

    /**
     * Spawn a given entity at a given spot on the grid, if possible.
     * @param entity (Interactable) the entity to spawn
     * @param coords (List<DiscreteCoordinates>) the coordinates of the target cells
     * @return true if the entity can be spawned at the given position
     */
    private boolean canSpawnEntity(AreaEntity entity, DiscreteCoordinates coords){
        return getOwnerArea().canEnterAreaCells(entity, Collections.singletonList(coords));
    }
    
    /**
     * Harm the player. Make sure that the HP level does not go below 0 or over MAX_HP.
     * @param damage (float) The amount of Health Points to remove from the ARPGPlayer
     */
    public void harm(float damage) {
        this.hp = Math.min(Math.max(this.hp - damage, 0), MAX_HP);
        hasBeenHurt = true;
        statusGui.updateHp(this.hp);
    }

    /**
     * Heal the player.
     * @param hp (float) The amount of Health Points to add from the ARPGPlayer
     */
    public void heal(float hp) {
        this.hp = Math.min(this.hp + hp, MAX_HP);
        statusGui.updateHp(this.hp);
    }

    public boolean isDead(){
        return hp <= 0;
    }
    
    // MARK:- Inventory.Holder
    
    @Override
    public boolean possess(InventoryItem item) {
        return inventory.contains(item);
    }
    
    // MARK:- Interactable
    
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
    
    // MARK:- Interactor
    
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        // The ARPGPlayer wants view interaction if the 'E' key is pressed,
        // or if he is attacking with his Sword
        return getOwnerArea().getKeyboard().get(Keyboard.E).isPressed() || state == State.ATTACKING_WITH_SWORD;
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
