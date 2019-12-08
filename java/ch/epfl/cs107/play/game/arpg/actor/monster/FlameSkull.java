package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class FlameSkull extends Monster implements FlyableEntity {
    
    private static class FlameSkullHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(ARPGPlayer player) {
            player.harm(DAMAGE);
        }
    
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }
    
        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }
        
        public void interactWith(Monster monster) {
            monster.harm(Vulnerability.FIRE, DAMAGE);
        }
        
    }
    
    /// The maximum Health Points
    private static final float MAX_HP = 2f;
    /// The minimum and maximum lifetimes, respectively
    private static final float MIN_LIFE_TIME = 2f, MAX_LIFE_TIME = 20f;
    
    /// The actual lifetime of the FlameSkull
    private float lifetime;
    
    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;
    
    /// The damage the FlameSkull deals
    private static final float DAMAGE = 1.5f;
    
    private static final float SIZE = 2f;
    
    /// The coordinates of the last Cell where damage has been dealt
    private DiscreteCoordinates lastCellDamagedCoordinates;
    
    /// The Interaction handler
    private FlameSkullHandler handler;
    
    /// Animations array
    private Animation[] animations;
    /// Index of the current animation in the above-mentioned array
    private int animationIndex;
    /// Animation duration in number of frames
    private static final int ANIMATION_DURATION = 16;
    
    /**
     * Default FlameSkull constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    public FlameSkull(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, Vulnerability.PHYSICAL, Vulnerability.MAGIC);
        randomlyOrientate();
        
        handler = new FlameSkullHandler();
    
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/flameSkull", 3,
                SIZE, SIZE, this, 32, 32,
                new Vector(-SIZE/4, -SIZE/4),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animations = RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
        animationIndex = getOrientation().ordinal();
     
        // We choose a random value between MIN_LIFE_TIME and MAX_LIFE_TIME
        lifetime = MIN_LIFE_TIME +
                ((MAX_LIFE_TIME - MIN_LIFE_TIME) * RandomGenerator.getInstance().nextFloat());
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (getState() == State.ALIVE) {
            lifetime -= deltaTime;
            if (lifetime <= 0) {
                die();
                return;
            }
    
            randomlyMove();
    
            // Compute the index of the animation to draw
            animationIndex = getOrientation().ordinal();
            if (isDisplacementOccurs()) {
                animations[animationIndex].update(deltaTime);
            } else {
                animations[animationIndex].reset();
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (getState() == State.ALIVE) {
            animations[animationIndex].draw(canvas);
        }
    }
    
    /**
     * This function helps to make sure that we haven't already dealt damage to the current cell,
     * in order not to do it multiple times.
     * @return (boolean) a boolean indicating if we changed cell since the last damage
     */
    private boolean hasChangedCellSinceLastDamage() {
        if (lastCellDamagedCoordinates == null) {
            return true;
        }
        
        return lastCellDamagedCoordinates.x != getCurrentMainCellCoordinates().x ||
                lastCellDamagedCoordinates.y != getCurrentMainCellCoordinates().y;
    }
    
    /**
     * Randomly orientate the Monster
     */
    private void randomlyOrientate() {
        // TODO: contradiction in the subject; choose among the 4 orientations, or CHANGE the orientation ?
        // For now, we just choose among the 4 orientations... that means, including the current one
        // Thus, it is not ALWAYS a real 'change'
        int randomIndex = RandomGenerator.getInstance().nextInt(4);
        orientate(Orientation.fromInt(randomIndex));
    }
    
    /**
     * Randomly move the monster, and keep track if we changed moved from a Cell
     */
    private void randomlyMove() {
        if (!isDisplacementOccurs()) {
            if (RandomGenerator.getInstance().nextFloat() < PROBABILITY_TO_CHANGE_ORIENTATION) {
                randomlyOrientate();
            }
    
            move(ANIMATION_DURATION);
        }
    }
    
    // MARK:- Monster
    
    @Override
    float getMaxHp() {
        return MAX_HP;
    }
    
    // MARK:- Interactable
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
    // MARK:- Interactor
    
    @Override
    public void interactWith(Interactable other) {
        lastCellDamagedCoordinates = getCurrentMainCellCoordinates();
        other.acceptInteraction(handler);
    }
    
    @Override
    public boolean wantsCellInteraction() {
        // We check if the monster has changed cell, in order not to deal the damage mutliple times
        return (getState() == State.ALIVE && hasChangedCellSinceLastDamage());
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }
    
}
