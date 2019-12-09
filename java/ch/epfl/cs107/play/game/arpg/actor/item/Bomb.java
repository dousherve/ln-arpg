package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.monster.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {
    
    private static class BombHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }

        @Override
        public void interactWith(ARPGPlayer player) {
            player.harm(DAMAGE);
        }

        @Override
        public void interactWith(LogMonster monster) {
            monster.harm(Monster.Vulnerability.FIRE, DAMAGE);
        }

        @Override
        public void interactWith(FlameSkull skull) {
            skull.harm(Monster.Vulnerability.PHYSICAL, DAMAGE);
        }
        
    }
    
    private enum State {
        READY, EXPLODING, HAS_EXPLODED
    }
    
    private static final float DAMAGE = 2f;
    
    /// The default remaining time before the Bomb explodes
    private static final int DEFAULT_TIMER_VALUE = 100;
    /// The remaining time before the Bomb explodes
    private int timer;
    /// Whether the bomb has already dealt damage or not
    private boolean hasDealtCellDamage, hasDealtViewDamage;
    
    /// The Interaction Handler
    private final BombHandler handler;
    
    /// The current state of the Bomb
    private State state;
    
    private Sprite sprite;
    private Animation explosionAnimation;
    private static final int ANIMATION_DURATION = 4;
    
    /**
     * Default Bomb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        
        handler = new BombHandler();
    
        sprite = new RPGSprite("zelda/bomb", 1, 1, this,
                new RegionOfInterest(0, 0, 16, 16));
    
        Sprite[] explosionSprites = new Sprite[7];
        for (int i = 0; i < explosionSprites.length; ++i) {
            explosionSprites[i] = new RPGSprite("zelda/explosion", 2, 2, this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, -0.5f));
        }
        explosionAnimation = new Animation(ANIMATION_DURATION / 2, explosionSprites, false);
    
        timer = DEFAULT_TIMER_VALUE;
        hasDealtCellDamage = false;
        hasDealtViewDamage = false;
        
        state = State.READY;
    }
    
    @Override
    public void draw(Canvas canvas) {
        switch (state) {
            case READY:
                sprite.draw(canvas);
                break;
            case EXPLODING:
                if (!explosionAnimation.isCompleted()) {
                    explosionAnimation.draw(canvas);
                } else {
                    state = State.HAS_EXPLODED;
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void update(float deltaTime) {
        switch (state) {
            case READY:
                --timer;
                if (timer <= 0) {
                    explode();
                }
                break;
            case EXPLODING:
                explosionAnimation.update(deltaTime);
    
                if (explosionAnimation.isCompleted()) {
                    state = State.HAS_EXPLODED;
                }
                break;
            case HAS_EXPLODED:
                // Unregister the bomb
                getOwnerArea().unregisterActor(this);
                break;
        }
    }
    
    /**
     * BOOOOOM.
     */
    public void explode() {
        state = State.EXPLODING;
    }
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    // MARK:- Interactor
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }
    
    @Override
    public boolean wantsCellInteraction() {
        if (!hasDealtCellDamage && state == State.EXPLODING) {
            hasDealtCellDamage = true;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        if (!hasDealtViewDamage && state == State.EXPLODING) {
            hasDealtViewDamage = true;
            return true;
        }
    
        return false;
    }
    
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
    // MARK:- Interactable
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }
    
    @Override
    public boolean isCellInteractable() {
        return true;
    }
    
    @Override
    public boolean isViewInteractable() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
