package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
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
    
    enum MonsterState {
        ALIVE, VANISHING, DEAD
    }
    
    /// The vulnerabilities of the current Monster
    private Vulnerability[] vulnerabilities;
    /// The Health Points of the Monster
    private float hp;
    
    /// Keep track of the state of the Monster
    private MonsterState monsterState;
    
    /// The coordinates of the last Cell where damage has been dealt
    private DiscreteCoordinates lastCellDamagedCoordinates;
    
    /// The vanish Animation
    private Animation vanishAnimation;
    private static final int VANISH_ANIMATION_DURATION = 2;
    
    /**
     * Default Monster constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param vulnerabilities (Vulnerability...) The vulnerabilities of the Monster
     */
    Monster(Area area, Orientation orientation, DiscreteCoordinates position, Vulnerability... vulnerabilities) {
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
        
        monsterState = MonsterState.ALIVE;
        
        enterArea(area, position);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (monsterState == MonsterState.VANISHING) {
            if (!vanishAnimation.isCompleted()) {
                vanishAnimation.update(deltaTime);
            } else {
                monsterState = MonsterState.DEAD;
    
                // Drop the items to drop at death
                for (ARPGCollectableAreaEntity entity : getItemsToDropAtDeath()) {
                    getOwnerArea().registerActor(entity);
                }
                
                leaveArea();
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (monsterState == MonsterState.VANISHING) {
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
        if (monsterState == MonsterState.DEAD) {
            return;
        }
        
        if (isVulnerableTo(vulnerability)) {
            hp = Math.max(hp - damage, 0);
            // TODO: remove debug sout
            System.out.println(getClass().getSimpleName() + 
                    " harmed: " + vulnerability.name() + " ; new HP: " + hp);
            if (hp <= 0) {
                die();
            }
        }
    }
    
    /**
     * The actions to be executed when the Monster dies.
     */
    void die() {
        monsterState = MonsterState.VANISHING;
        
        leaveArea();
    }
    
    /**
     *
     * @return (State) The current state of the Monster
     */
    MonsterState getMonsterState() {
        return monsterState;
    }
    
    /**
     * This function helps to make sure that we haven't already dealt damage to the current cell,
     * in order not to do it multiple times.
     * @return (boolean) a boolean indicating if we changed cell since the last damage
     */
    boolean hasChangedCellSinceLastDamage() {
        if (lastCellDamagedCoordinates == null) {
            return true;
        }
        
        return lastCellDamagedCoordinates.x != getCurrentMainCellCoordinates().x ||
                lastCellDamagedCoordinates.y != getCurrentMainCellCoordinates().y;
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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public boolean takeCellSpace() {
        return (monsterState == MonsterState.ALIVE);
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
    public void interactWith(Interactable other) {
        lastCellDamagedCoordinates = getCurrentMainCellCoordinates();
    }
    
    @Override
    public boolean wantsCellInteraction() {
        return false;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }
    
}
