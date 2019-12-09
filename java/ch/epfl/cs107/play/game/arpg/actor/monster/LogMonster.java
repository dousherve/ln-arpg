package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogMonster extends Monster {
    
    private class LogMonsterHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(ARPGPlayer player) {
            if (state == LogMonsterState.IDLE) {
                state = LogMonsterState.ATTACKING;
                System.out.println(state);
            } else if (state == LogMonsterState.ATTACKING) {
                player.harm(DAMAGE);
                hasReachedTarget = true;
                state = LogMonsterState.FALLING_ASLEEP;
                System.out.println(state);
            }
        }
        
    }
    
    private enum LogMonsterState {
        IDLE, ATTACKING, FALLING_ASLEEP, SLEEPING, WAKING_UP
    }
    
    /// The maximum Health Points
    private static final float MAX_HP = 2f;
    
    /// The minimum and maximum sleeping durations, respectively
    private static final float MIN_SLEEPING_DURATION = 5f, MAX_SLEEPING_DURATION = 10f;
    
    /// The actual lifetime of the LogMonster
    private float lifetime;
    
    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;
    
    /// The damage the LogMonster deals
    private static final float DAMAGE = 2f;
    private boolean hasReachedTarget;
    
    /// The size of the LogMonster
    private static final float SIZE = 2f;
    
    /// The Interaction handler
    private LogMonsterHandler handler;
    
    /// The LogMonster state
    private LogMonsterState state;
    
    /// The number of facing Cells to add in his FOV when not attacking
    private static final int DEFAULT_FOV_CELLS_COUNT = 8;
    
    /// The maximum inactivity durations
    private static final float MAX_INACTIVITY_DURATION = 24f;
    private float inactivityDuration;
    
    // MARK:- Animations
    
    /// Moving animations array
    private Animation[] movingAnimations;
    /// Index of the current animation in the above-mentioned array
    private int movingAnimationIndex;
    /// Moving animation duration in number of frames
    private static final int MOVING_ANIMATION_DURATION = 16;
    
    private Animation sleepingAnimation;
    private static final int SLEEPING_ANIMATION_DURATION = 16;
    
    private Animation wakingUpAnimation;
    private static final int WAKING_ANIMATION_DURATION = 16;
    
    /**
     * Default LogMonster constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    public LogMonster(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, Vulnerability.PHYSICAL, Vulnerability.FIRE);
        
        handler = new LogMonsterHandler();
        state = LogMonsterState.IDLE;
        inactivityDuration = 0f;
        
        hasReachedTarget = false;
    
        setupAnimations();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (getMonsterState() != MonsterState.ALIVE) {
            return;
        }
        
        inactivityDuration = Math.max(inactivityDuration - deltaTime, 0);
    
        switch (state)  {
            case SLEEPING:
                sleepingAnimation.update(deltaTime);
                break;
        
            case WAKING_UP:
                wakingUpAnimation.update(deltaTime);
                break;
        
            default:
                movingAnimationIndex = getOrientation().ordinal();
                if (isDisplacementOccurs()) {
                    movingAnimations[movingAnimationIndex].update(deltaTime);
                } else {
                    movingAnimations[movingAnimationIndex].reset();
                }
        }
        
        if (inactivityDuration <= 0) {
            switch (state) {
                case IDLE:
                    if (!isDisplacementOccurs()) {
                        randomlyMove();
                        inactivityDuration = RandomGenerator.getInstance().nextFloat() * MAX_INACTIVITY_DURATION;
                    }
                    break;
                    
                case ATTACKING:
                    inactivityDuration = 0;
                    move(MOVING_ANIMATION_DURATION);
                    if (!isDisplacementOccurs() && hasReachedTarget) {
                        state = LogMonsterState.FALLING_ASLEEP;
                        hasReachedTarget = false;
                        System.out.println(state);
                    }
                    break;
                    
                case FALLING_ASLEEP:
                    inactivityDuration = MIN_SLEEPING_DURATION +
                            (MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION) * RandomGenerator.getInstance().nextFloat();
                    state = LogMonsterState.SLEEPING;
                    System.out.println(state);
                    break;
                    
                case SLEEPING:
                    if (inactivityDuration <= 0) {
                        state = LogMonsterState.WAKING_UP;
                        System.out.println(state);
                        wakingUpAnimation.reset();
                    }
                    break;
                    
                case WAKING_UP:
                    if (wakingUpAnimation.isCompleted()) {
                        state = LogMonsterState.IDLE;
                        System.out.println(state);
                        wakingUpAnimation.reset();
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (getMonsterState() == MonsterState.ALIVE) {
            
            switch (state) {
                case SLEEPING:
                    sleepingAnimation.draw(canvas);
                    break;
                    
                case WAKING_UP:
                    wakingUpAnimation.draw(canvas);
                    break;
                    
                default:
                    movingAnimations[movingAnimationIndex].draw(canvas);
                    break;
            }
            
        }
    }
    
    // MARK:- Specific LogMonster methods
    
    /**
     * Setup the animations.
     */
    private void setupAnimations() {
        // Moving Animation
        Sprite[][] movingSprites = RPGSprite.extractSprites("zelda/logMonster", 4,
                SIZE, SIZE, this, 32, 32,
                new Vector(-0.5f, 0),
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION, movingSprites);
        movingAnimationIndex = getOrientation().ordinal();
        
        // Sleeping Animation
        Sprite[] sleepingSprites = new Sprite[4];
        for (int i = 0; i < sleepingSprites.length; ++i) {
            sleepingSprites[i] = new RPGSprite("zelda/logMonster.sleeping", SIZE, SIZE, this,
                    new RegionOfInterest(0, 32 * i, 32, 32),
                    new Vector(-0.5f, 0));
        }
        sleepingAnimation = new Animation(SLEEPING_ANIMATION_DURATION, sleepingSprites);
        
        // Waking-up Animation
        Sprite[] wakingUpSprites = new Sprite[3];
        for (int i = 0; i < wakingUpSprites.length; ++i) {
            wakingUpSprites[i] = new RPGSprite("zelda/logMonster.wakingUp", SIZE, SIZE, this,
                    new RegionOfInterest(0, 32 * i, 32, 32),
                    new Vector(-0.5f, 0));
        }
        wakingUpAnimation = new Animation(WAKING_ANIMATION_DURATION, wakingUpSprites, false);
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
            
            move(MOVING_ANIMATION_DURATION);
        }
    }
    
    // MARK:- Monster
    
    @Override
    float getMaxHp() {
        return 5f;
    }
    
    @Override
    protected List<ARPGCollectableAreaEntity> getItemsToDropAtDeath() {
        return Collections.singletonList(
                new Coin(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates())
        );
    }
    
    // MARK:- Interactable
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
    // MARK:- Interactor
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (state == LogMonsterState.ATTACKING) {
            return Collections.singletonList(
                    getCurrentMainCellCoordinates().jump(getOrientation().toVector())
            );
        } else {
            List<DiscreteCoordinates> coords = new ArrayList<>();
            
            // We add a constant number of facing cells to the FOV
            for (int i = 1; i <= DEFAULT_FOV_CELLS_COUNT; ++i) {
                coords.add(getCurrentMainCellCoordinates().jump(
                        getOrientation().toVector().mul(i)
                ));
            }
            
            return coords;
        }
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return (state == LogMonsterState.IDLE || state == LogMonsterState.ATTACKING);
    }
    
    @Override
    public void interactWith(Interactable other) {
        super.interactWith(other);
        
        other.acceptInteraction(handler);
    }
    
}
