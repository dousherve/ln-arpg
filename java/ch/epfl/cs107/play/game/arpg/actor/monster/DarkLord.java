package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Or;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class DarkLord extends Monster {

    private class DarkLordHandler implements ARPGInteractionVisitor {

    }

    private enum DarkLordState {
        IDLE, ATTACKING, SUMMONING, CASTING_TELEPORT_SPELL, TELEPORTING
    }

    /// The maximum Health Points
    private static final float MAX_HP = 5f;
    
    /// The radius of its action field
    private static final int ACTION_RADIUS = 3;

    /// The size of the DarkLord
    private static final float SIZE = 3f;

    /// The Interaction handler
    private DarkLordHandler handler;

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

    /**
     * Default DarkLord constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, MAX_HP, Vulnerability.MAGIC);
        
        handler = new DarkLordHandler();
        setupAnimations();
    }

    // MARK:- Specific DarkLord methods

    /**
     * Setup the animations.
     */
    private void setupAnimations(){
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
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
    }
    
    // MARK:- Interactable
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> coords = new ArrayList<>();
        final DiscreteCoordinates CURRENT_COORDS = getCurrentMainCellCoordinates();
        final int BOTTOM_LEFT = -ACTION_RADIUS * CURRENT_COORDS.x;
        final int END_COORD = CURRENT_COORDS.x + ACTION_RADIUS;
    
        for (int i = BOTTOM_LEFT; i < END_COORD; ++i) {
            for (int j = BOTTOM_LEFT; j < END_COORD; ++j) {
                if (
                    i >= 0 && i < getOwnerArea().getWidth() &&
                    j >= 0 && j < getOwnerArea().getHeight() &&
                    i != CURRENT_COORDS.x && j != CURRENT_COORDS.y
                ) {
                    coords.add(new DiscreteCoordinates(i, j));
                }
            }
        }
        
        // TODO: remove debug sout
        System.out.println(coords.size());
        
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
