package ch.epfl.cs107.play.game.arpg.actor.attacker;

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
                inactivityDuration = 0;

            } else if (state == LogMonsterState.ATTACKING) {
                player.harm(DAMAGE);
                state = LogMonsterState.FALLING_ASLEEP;
                isBlocked = false;
            }
        }
        
    }
    
    private enum LogMonsterState {
        IDLE, ATTACKING, FALLING_ASLEEP, SLEEPING, WAKING_UP
    }
    
    /// The maximum Health Points
    private static final float MAX_HP = 5f;
    
    /// The minimum and maximum sleeping durations, respectively
    private static final float MIN_SLEEPING_DURATION = 5f, MAX_SLEEPING_DURATION = 10f;
    
    /// The actual lifetime of the LogMonster
    private float lifetime;
    
    /// The damage the LogMonster deals
    private static final float DAMAGE = 2f;
    
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

    /// To check if the monster is blocked by an non-walkable cell while attacking
    private boolean isBlocked = false;
    
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

    /// Animation duration in number of frames
    private static final int ANIMATION_DURATION = 16;
    
    /**
     * Default LogMonster constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    public LogMonster(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, MAX_HP, Vulnerability.PHYSICAL, Vulnerability.FIRE);
        
        handler = new LogMonsterHandler();
        state = LogMonsterState.IDLE;
        inactivityDuration = 0f;
        
        setupAnimations();
        randomlyOrientate();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
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
                        randomlyMove(ANIMATION_DURATION);
                        inactivityDuration = RandomGenerator.getInstance().nextFloat() * MAX_INACTIVITY_DURATION;
                    }
                    break;
                    
                case ATTACKING:
                    if(isBlocked){
                        isBlocked = false;
                        state = LogMonsterState.FALLING_ASLEEP;
                    }

                    if(!move(MOVING_ANIMATION_DURATION) && !isDisplacementOccurs()){
                        isBlocked = true;
                    }

                    break;
                    
                case FALLING_ASLEEP:
                    inactivityDuration = MIN_SLEEPING_DURATION +
                            (MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION) * RandomGenerator.getInstance().nextFloat();
                    state = LogMonsterState.SLEEPING;
                    break;
                    
                case SLEEPING:
                    if (inactivityDuration <= 0) {
                        state = LogMonsterState.WAKING_UP;
                        wakingUpAnimation.reset();
                    }
                    break;
                    
                case WAKING_UP:
                    if (wakingUpAnimation.isCompleted()) {
                        state = LogMonsterState.IDLE;
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
        
        // TODO: remove the if ?
        if (isAlive()) {
            
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
     * @return (boolean) a boolean indicating if we can go to the facing cell
     */
    private boolean canGoToNextCell() {
        return getOwnerArea().canEnterAreaCells(this,
                Collections.singletonList(
                        getCurrentMainCellCoordinates().jump(getOrientation().toVector())
                )
        );
    }
    
    // MARK:- Monster
    
    @Override
    protected ARPGCollectableAreaEntity getItemToDropAtDeath() {
        return new Coin(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
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
        other.acceptInteraction(handler);
    }
    
}
