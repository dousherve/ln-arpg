package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
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
    
    /// The radius of its action field
    private static final int ACTION_RADIUS = 3;

    /// The size of the DarkLord
    private static final float SIZE = 2f;

    /// The Interaction handler
    private DarkLordHandler handler;

    /// The DarkLord state
    private DarkLordState state;

    /// The maximum inactivity durations
    private static final float MAX_INACTIVITY_DURATION = 24f;
    private float inactivityDuration;

    // MARK:- Attack

    private static final float PROBABILITY_TO_ATTACK = .7f;

    private static final int MIN_SPELL_WAIT_DURATION = 120;
    private static final int MAX_SPELL_WAIT_DURATION = 240;
    //counter for number of cycle
    private int cycleCount;
    // spell reloading time in cycle of simulation
    private int spellWaitDuration;

    // MARK:- Animations

    /// Moving animations array
    private Animation[] movingAnimations;
    /// Index of the current animation in the above-mentioned array
    private int movingAnimationIndex;
    /// Moving animation duration in number of frames
    private static final int MOVING_ANIMATION_DURATION = 16;

    /// Attacking animations array
    private Animation[] attackingAnimations;
    ///Index of the current animation in the above-mentioned array
    private int attackingAnimationIndex;
    /// Moving animation duration in number of frames
    private static final int ATTACKING_ANIMATION_DURATION = 16;

    private static int ANIMATION_DURATION = 16;

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
        randomizeSpellWaitDuration();
        inactivityDuration = 0f;

        setupAnimations();
    }

    // MARK:- Specific DarkLord methods

    /**
     * set spellWaitDuration to random valute between MIN_SPELL_WAIT_DURATION and MAX_SPELL_WAIT_DURATION
     */
    private void randomizeSpellWaitDuration(){
        spellWaitDuration = MIN_SPELL_WAIT_DURATION + (int)((MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION)*RandomGenerator.getInstance().nextFloat());
    }

    /**
     *
     * @return random orientation towards which there is an available cell for a FireSpell
     */
    private Orientation getRandomFreeCellOrientation() {
        List<Orientation> possibleOrientations = new ArrayList<>();

        FireSpell fireSpell = new FireSpell(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates(), 0);
        for (Orientation orientation : Orientation.values()) {
            if (canSummon(fireSpell, getCurrentMainCellCoordinates().jump(orientation.toVector()))) {

                possibleOrientations.add(orientation);
            }
        }
        if (possibleOrientations.isEmpty()) {
            return getOrientation();
        } else {
            return possibleOrientations.get(RandomGenerator.getInstance().nextInt(possibleOrientations.size()));
        }
    }

    /**
     * Setup the animations.
     */
    private void setupAnimations() {
        //Moving Animation
        Sprite[][] movingSprites = RPGSprite.extractSprites("zelda/darkLord", 3,
                SIZE, SIZE, this, 32,32,
                new Vector(-0.5f,0),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION, movingSprites);
        movingAnimationIndex = getOrientation().ordinal();

        //Attacking Animation
        Sprite[][] attackingSprites = RPGSprite.extractSprites("zelda/darkLord.spell",3,
                SIZE, SIZE, this, 32, 32,
                new Vector(-0.5f,0),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        attackingAnimations = RPGSprite.createAnimations(ATTACKING_ANIMATION_DURATION, attackingSprites);
        attackingAnimationIndex = getOrientation().ordinal();
    }

    /**
     *
     * @param entity to summon
     * @param coords of target cell
     * @return true if the entity can be summoned in the cell
     */
    private boolean canSummon(Interactable entity, DiscreteCoordinates coords){
        return getOwnerArea().canEnterAreaCells(entity, Collections.singletonList(coords));
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        inactivityDuration = Math.max(inactivityDuration - deltaTime, 0);


        ++cycleCount;
        if(cycleCount >= spellWaitDuration){
            cycleCount = 0;
            randomizeSpellWaitDuration();

            if(RandomGenerator.getInstance().nextFloat() < PROBABILITY_TO_ATTACK){
                state = DarkLordState.ATTACKING;
            }else{
                state = DarkLordState.SUMMONING;
            }

            Orientation randomOrientation = getRandomFreeCellOrientation();
            if (randomOrientation != getOrientation()) {

            }
            orientate(getRandomFreeCellOrientation());
            System.out.println(getOrientation());
        }

        switch (state){
            case CASTING_TELEPORT_SPELL:
                break;

            case SUMMONING:
                break;

            default:
                movingAnimationIndex = getOrientation().ordinal();
                if (isDisplacementOccurs()) {
                    movingAnimations[movingAnimationIndex].update(deltaTime);
                } else {
                    movingAnimations[movingAnimationIndex].reset();
                }
        }

        if(inactivityDuration <= 0) {
            DiscreteCoordinates coords = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

            switch (state) {
                case IDLE:
                    if (!isDisplacementOccurs()) {
                        randomlyMove(ANIMATION_DURATION);
                        inactivityDuration = RandomGenerator.getInstance().nextFloat() * MAX_INACTIVITY_DURATION;
                    }
                    break;

                case ATTACKING:
                    FireSpell fireSpell = new FireSpell(getOwnerArea(), getOrientation(), coords, 0);

                    if (canSummon(fireSpell, coords)){
                        getOwnerArea().registerActor(fireSpell);
                    }
                    state = DarkLordState.IDLE;
                    break;

                case SUMMONING:
                    FlameSkull skull = new FlameSkull(getOwnerArea(), coords, getOrientation());

                    if (canSummon(skull, coords)){
                        getOwnerArea().registerActor(skull);
                    }
                    state = DarkLordState.IDLE;
                    break;

                case TELEPORTING:
                    break;

                case CASTING_TELEPORT_SPELL:
                    break;

                default:
                    break;
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        switch (state){
            default:
                movingAnimations[movingAnimationIndex].draw(canvas);
        }

    }
    
    // MARK:- Interactable
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> coords = new ArrayList<>();
        final DiscreteCoordinates CURRENT_COORDS = getCurrentMainCellCoordinates();
    
        for (int i = CURRENT_COORDS.x - ACTION_RADIUS; i <= CURRENT_COORDS.x + ACTION_RADIUS; ++i) {
            for (int j = CURRENT_COORDS.y - ACTION_RADIUS; j <= CURRENT_COORDS.y + ACTION_RADIUS; ++j) {
                if (
                    i >= 0 && i < getOwnerArea().getWidth() &&
                    j >= 0 && j < getOwnerArea().getHeight() && !(i == CURRENT_COORDS.x && j == CURRENT_COORDS.y)
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
        return getMonsterState() == MonsterState.ALIVE;
    }
}
