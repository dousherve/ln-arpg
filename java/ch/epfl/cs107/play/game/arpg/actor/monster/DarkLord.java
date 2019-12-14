package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DarkLord extends Monster {

    private class DarkLordHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            if (state != DarkLordState.TELEPORTING) {
                state = DarkLordState.CASTING_TELEPORT_SPELL;
            }
        }
        
    }

    private enum DarkLordState {
        IDLE, ATTACKING, SUMMONING, CASTING_TELEPORT_SPELL, TELEPORTING
    }

    /// The maximum Health Points
    private static final float MAX_HP = 5f;

    /// The size of the DarkLord
    private static final int SIZE = 2;

    /// The Interaction handler
    private final DarkLordHandler handler;

    /// The DarkLord state
    private DarkLordState state;

    /// The maximum inactivity duration
    private static final float MAX_INACTIVITY_DURATION = 2f;
    
    private float inactivityDuration;

    // MARK:- Attack
    
    /// The radius of its action field
    private static final int ACTION_RADIUS = 3;
    
    private static final int FIRE_SPELL_STRENGTH = 3;

    private static final float PROBABILITY_TO_ATTACK = 1f;

    private static final int MIN_SPELL_WAIT_DURATION = 240;
    private static final int MAX_SPELL_WAIT_DURATION = 360;
    
    /// Keep track of the cycle count
    private int cycleCount;
    // Spell reloading time in number of cycles of simulation
    private int spellWaitDuration;

    // Teleportation
    private static final int TELEPORTATION_RADIUS = 5;
    private static final int MAX_TELEPORTATION_ATTEMPTS = 5;
    private int teleportationAttempts;
    
    // MARK:- Animations
    
    /// Index of the current animation
    private int animationIndex;
    
    /// Moving animations array
    private Animation[] movingAnimations;
    /// Moving animation duration in number of frames
    private static final int MOVING_ANIMATION_DURATION = 16;

    /// Attacking / spell / teleportation animations array
    private Animation[] attackingAnimations;
    /// Moving animation duration in number of frames
    private static final int ATTACKING_ANIMATION_DURATION = 16;

    /**
     * Default DarkLord constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, MAX_HP, Vulnerability.MAGIC);

        handler = new DarkLordHandler();

        state = DarkLordState.IDLE;
        cycleCount = 0;
        inactivityDuration = 0f;
        teleportationAttempts = 0;
    
        randomizeSpellWaitDuration();
        setupAnimations();
    }
    
    // MARK:- Monster
    
    @Override
    protected ARPGCollectableAreaEntity getItemToDropAtDeath() {
        return new CastleKey(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
    }
    
    // MARK:- Specific DarkLord methods

    /**
     * Set spellWaitDuration to a random value between 
     * MIN_SPELL_WAIT_DURATION and MAX_SPELL_WAIT_DURATION
     */
    private void randomizeSpellWaitDuration() {
        spellWaitDuration = MIN_SPELL_WAIT_DURATION + 
                RandomGenerator.getInstance().nextInt(MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION);
    }

    /**
     * Return a random orientation among those towards which it is possible for a
     * cell to be invested by a FireSpell
     * 
     * @return (Orientation) the above-mentioned Orientation
     */
    private Orientation getRandomFreeCellOrientation() {
        List<Orientation> possibleOrientations = new ArrayList<>();
        
        for (Orientation orientation : Orientation.values()) {
            FireSpell fireSpell = new FireSpell(getOwnerArea(), orientation,
                    getFacingCellCoordinates(), FIRE_SPELL_STRENGTH);
            if (canSummon(fireSpell, getFacingCellCoordinates())) {
                possibleOrientations.add(orientation);
            }
        }
        
        if (!possibleOrientations.isEmpty()) {
            return possibleOrientations.get(
                    RandomGenerator.getInstance().nextInt(possibleOrientations.size())
            );
        } else {
            return getOrientation();
        }
    }
    
    /**
     * @return (DiscreteCoordinates) the coordinates of the cell currently facing the monster
     */
    private DiscreteCoordinates getFacingCellCoordinates() {
        return getCurrentMainCellCoordinates().jump(getOrientation().toVector());
    }

    /**
     * Setup the animations.
     */
    private void setupAnimations() {
        // Moving Animation
        Sprite[][] movingSprites = RPGSprite.extractSprites("zelda/darkLord", 3,
                SIZE, SIZE, this, 32,32,
                new Vector(-0.5f,0),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION, movingSprites);
    
        // Attacking Animation
        Sprite[][] attackingSprites = RPGSprite.extractSprites("zelda/darkLord.spell",3,
                SIZE, SIZE, this, 32, 32,
                new Vector(-0.5f,0),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        attackingAnimations = RPGSprite.createAnimations(ATTACKING_ANIMATION_DURATION, attackingSprites, false);
        
        animationIndex = getOrientation().ordinal();
    }

    /**
     *
     * @param entity (Interactable) the entity to summon
     * @param coords (DiscreteCoordinates) the coordinates of the target cell
     * @return true if the entity can be summoned at the given position
     */
    private boolean canSummon(Interactable entity, DiscreteCoordinates coords) {
        return getOwnerArea().canEnterAreaCells(entity, Collections.singletonList(coords));
    }
    
    /**
     *
     * @param entity (Interactable) the entity to summon
     * @param coords (List<DiscreteCoordinates>) the coordinates of the target cells
     * @return true if the entity can be summoned at the given position
     */
    private boolean canSummon(Interactable entity, List<DiscreteCoordinates> coords) {
        return getOwnerArea().canEnterAreaCells(entity, coords);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        inactivityDuration = Math.max(inactivityDuration - deltaTime, 0);

        // Cycle handling: prepare to attack
        
        ++cycleCount;
        if (cycleCount >= spellWaitDuration) {
            cycleCount = 0;
            System.out.println("PREPARING");
            randomizeSpellWaitDuration();

            if (RandomGenerator.getInstance().nextFloat() < PROBABILITY_TO_ATTACK) {
                state = DarkLordState.ATTACKING;
                
                Orientation randomOrientation = getRandomFreeCellOrientation();
                if (randomOrientation != getOrientation()) {
                    orientate(randomOrientation);
                    System.out.println(getOrientation());
                }
                
            } else {
                state = DarkLordState.SUMMONING;
            }
        }
        
        // Behave according to the current state
    
        final DiscreteCoordinates FACING_CELL_COORDS = 
                getCurrentMainCellCoordinates().jump(getOrientation().toVector());
    
        switch (state) {
            case IDLE:
                if (inactivityDuration <= 0 && !isDisplacementOccurs()) {
                    randomlyMove(MOVING_ANIMATION_DURATION);
                    inactivityDuration = RandomGenerator.getInstance().nextFloat() * MAX_INACTIVITY_DURATION;
                }
                break;
        
            case ATTACKING:
                if (attackingAnimations[animationIndex].isCompleted()) {
                    FireSpell fireSpell = new FireSpell(getOwnerArea(), getOrientation(), FACING_CELL_COORDS, 3, true);
    
                    if (canSummon(fireSpell, fireSpell.getCurrentCells())) {
                        getOwnerArea().registerActor(fireSpell);
                    }
    
                    state = DarkLordState.IDLE;
                }
                break;
        
            case SUMMONING:
                if (attackingAnimations[animationIndex].isCompleted()) {
                    FlameSkull skull = new FlameSkull(getOwnerArea(), getOrientation(), FACING_CELL_COORDS);
    
                    if (canSummon(skull, skull.getCurrentCells())) {
                        getOwnerArea().registerActor(skull);
                    }
    
                    state = DarkLordState.IDLE;
                }
                break;
    
            case CASTING_TELEPORT_SPELL:
                if (!isDisplacementOccurs() && attackingAnimations[animationIndex].isCompleted()) {
                    resetMotion();
                    state = DarkLordState.TELEPORTING;
                }
                break;
        
            case TELEPORTING:
                inactivityDuration = MAX_INACTIVITY_DURATION;
                
                do {
                    // TODO: remove debug sout
                    System.out.println(teleportationAttempts);
                    // Delta X, Delta Y
                    final int DX = RandomGenerator.getInstance().nextInt(TELEPORTATION_RADIUS * 2) - TELEPORTATION_RADIUS;
                    final int DY = RandomGenerator.getInstance().nextInt(TELEPORTATION_RADIUS * 2) - TELEPORTATION_RADIUS;
                    System.out.println("dx " + DX + " dy " + DY);
                
                    final DiscreteCoordinates mainTargetCoords = new DiscreteCoordinates(
                            getCurrentMainCellCoordinates().x + DX,
                            getCurrentMainCellCoordinates().y + DY
                    );
    
                    if (       
                            !isDisplacementOccurs() &&
                            mainTargetCoords.x >= 0 && mainTargetCoords.x < getOwnerArea().getWidth() &&
                            mainTargetCoords.y >= 0 && mainTargetCoords.y < getOwnerArea().getHeight() &&
                            getOwnerArea().canEnterAreaCells(this, Collections.singletonList(mainTargetCoords))
                    ) {

                        getOwnerArea().leaveAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates()));
                        setCurrentPosition(mainTargetCoords.toVector());
                        getOwnerArea().enterAreaCells(this, Collections.singletonList(mainTargetCoords));
    
                        attackingAnimations[animationIndex].reset();
                        state = DarkLordState.IDLE;
                        break;
                    }
                    
                    ++teleportationAttempts;
                
                } while (teleportationAttempts < MAX_TELEPORTATION_ATTEMPTS);
                
                teleportationAttempts = 0;
                state = DarkLordState.IDLE;
                break;
        
            default:
                break;
        }
    
        // Update the animations
    
        if (state == DarkLordState.IDLE) {
            attackingAnimations[animationIndex].reset();
        
            if (isDisplacementOccurs()) {
                movingAnimations[animationIndex].update(deltaTime);
            } else {
                movingAnimations[animationIndex].reset();
            }
        } else {
            attackingAnimations[animationIndex].update(deltaTime);
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (isAlive()) {
            
            if (state == DarkLordState.IDLE) {
                movingAnimations[animationIndex].draw(canvas);
            } else {
                attackingAnimations[animationIndex].draw(canvas);
            }
            
        }
        
    }
    
    // MARK:- Interactable
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        final DiscreteCoordinates CURRENT_COORDS = getCurrentMainCellCoordinates();
        
        final int X_START_COORD = CURRENT_COORDS.x - ACTION_RADIUS;
        final int X_END_COORD = CURRENT_COORDS.x + ACTION_RADIUS;
        final int Y_START_COORD = CURRENT_COORDS.y - ACTION_RADIUS;
        final int Y_END_COORD = CURRENT_COORDS.y + ACTION_RADIUS;
        
        List<DiscreteCoordinates> coords = new ArrayList<>();
    
        for (int i = X_START_COORD; i <= X_END_COORD; ++i) {
            for (int j = Y_START_COORD; j <= Y_END_COORD; ++j) {
                if (
                    i >= 0 && i < getOwnerArea().getWidth() &&
                    j >= 0 && j < getOwnerArea().getHeight() && 
                    !(i == CURRENT_COORDS.x && j == CURRENT_COORDS.y)
                ) {
                    coords.add(new DiscreteCoordinates(i, j));
                }
            }
        }
    
        return coords;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    // MARK:- Interactor

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return isAlive();
    }
    
    // MARK:- MovableAreaEntity
    
    @Override
    protected boolean orientate(Orientation orientation) {
        // We override the method to automatically update the animation index
        animationIndex = orientation.ordinal();
        return super.orientate(orientation);
    }
    
}
