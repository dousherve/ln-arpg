package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The top level of abstarction of a Monster
 */
public abstract class Monster extends MovableAreaEntity implements Interactor {
    
    public enum Vulnerability {
        PHYSICAL, FIRE, MAGIC
    }
    
    enum State {
        ALIVE, VANISHING, DEAD
    }
    
    /// The vulnerabilities of the current Monster
    private Vulnerability[] vulnerabilities;
    /// The Health Points of the Monster
    private float hp;
    
    /// Keep track of the state of the Monster
    private State state;
    
    /// The vanish Animation
    private Animation vanishAnimation;
    private static final int VANISH_ANIMATION_DURATION = 2;
    
    /**
     * Default Monster constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Monster(Area area, Orientation orientation, DiscreteCoordinates position, Vulnerability... vulnerabilities) {
        super(area, orientation, position);
        
        this.vulnerabilities = vulnerabilities;
        this.hp = getMaxHp();
    
        Sprite[] vanishSprites = new Sprite[7];
        for (int i = 0; i < vanishSprites.length; ++i) {
            vanishSprites[i] = new RPGSprite("zelda/vanish", 2f, 2f, this,
                    new RegionOfInterest(32 * i, 0, 32, 32),
                    new Vector(-0.5f, -0.5f));
        }
    
        vanishAnimation = new Animation(VANISH_ANIMATION_DURATION, vanishSprites, false);
        
        state = State.ALIVE;
        
        enterArea(area, position);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (state == State.VANISHING) {
            if (!vanishAnimation.isCompleted()) {
                vanishAnimation.update(deltaTime);
            } else {
                state = State.DEAD;
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (state == State.VANISHING) {
            vanishAnimation.draw(canvas);
        }
    }
    
    // MARK:- Specific Monster Methods
    
    /**
     *
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    private void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }
    
    /**
     * Leave an area by unregistering this Monster
     */
    private void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }
    
    /**
     *
     * @return (float) The maximum Health Points of the current Monster
     */
    abstract float getMaxHp();
    
    /**
     *
     * @return (List<ARPGCollectableAreaEntity>) The list of items to drop at death. Empty by default.
     */
    protected List<ARPGCollectableAreaEntity> getItemsToDropAtDeath() {
        return Collections.emptyList();
    }
    
    /**
     * Harm the current Monster, if it is vulnerable to the given vulnerability.
     * @param vulnerability (Vulnerability) The vulnerability to exploit
     * @param damage (float) The damage to
     */
    public void harm(Vulnerability vulnerability, float damage) {
        if (state == State.DEAD) {
            return;
        }
        
        if (isVulnerableTo(vulnerability)) {
            hp = Math.max(hp - damage, 0);
            // TODO: remove debug sout
            System.out.println(getClass().getSimpleName() + "harmed: " + vulnerability.name() +
                    " ; HP: " + damage);
        }
        
        if (hp <= 0) {
            die();
        }
    }
    
    /**
     * The actions to be executed when the Monster dies.
     */
    void die() {
        state = State.VANISHING;
        
        leaveArea();
        
        for (ARPGCollectableAreaEntity entity : getItemsToDropAtDeath()) {
            getOwnerArea().registerActor(entity);
        }
    }
    
    /**
     *
     * @return (State) The current state of the Monster
     */
    State getState() {
        return state;
    }
    
    /**
     *
     * @param vulnerability (Vulnerability) The vulnerability to check
     * @return (boolean) a boolean indicating if the monster is vulnerable to the given vulnerability
     */
    private boolean isVulnerableTo(Vulnerability vulnerability) {
        return Arrays.asList(vulnerabilities).contains(vulnerability);
    }
    
    // MARK:- Interactable
    
    @Override
    public boolean takeCellSpace() {
        return (state == State.ALIVE);
    }
    
    @Override
    public boolean isCellInteractable() {
        return true;
    }
    
    @Override
    public boolean isViewInteractable() {
        return true;
    }
    
    // MARK:- Interactor
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }
    
}
