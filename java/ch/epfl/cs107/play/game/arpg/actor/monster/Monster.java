package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
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
    
    /**
     * The different vulnerabilities a Monster can have.
     */
    public enum Vulnerability {
        PHYSICAL, FIRE, MAGIC
    }
    
    /**
     * The different states a Monster can be in at its top level.
     */
    protected enum MonsterState {
        ALIVE, VANISHING, DEAD
    }

    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;
    
    /// The vulnerabilities of the current Monster
    private Vulnerability[] vulnerabilities;
    /// The Health Points of the Monster
    private float hp;
    
    /// Keep track of the state of the Monster
    private MonsterState monsterState;
    
    /// The vanish Animation
    private Animation vanishAnimation;
    private static final int VANISH_ANIMATION_DURATION = 2;


    /**
     * Default Monster constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param maxHp       (float) The maximum HP
     * @param vulnerabilities (Vulnerability...) The vulnerabilities of the Monster
     */
    protected Monster(Area area, Orientation orientation, DiscreteCoordinates position, float maxHp, Vulnerability... vulnerabilities) {
        super(area, orientation, position);
        
        this.vulnerabilities = vulnerabilities;
        this.hp = maxHp;
    
        Sprite[] vanishSprites = new Sprite[7];
        for (int i = 0; i < vanishSprites.length; ++i) {
            vanishSprites[i] = new RPGSprite("zelda/vanish", 2f, 2f, this,
                    new RegionOfInterest(32 * i, 0, 32, 32),
                    new Vector(-0.5f, -0.5f)
            );
        }
    
        vanishAnimation = new Animation(VANISH_ANIMATION_DURATION, vanishSprites, false);
        
        monsterState = MonsterState.ALIVE;
        
        enterArea(area, position);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(wasHurt) {
            blink(deltaTime);
        }
        
        if (monsterState == MonsterState.VANISHING) {
            
            if (!vanishAnimation.isCompleted()) {
                vanishAnimation.update(deltaTime);
            } else {
                monsterState = MonsterState.DEAD;
    
                // Drop the item to drop at death
                if (getItemToDropAtDeath() != null) {
                    getOwnerArea().registerActor(getItemToDropAtDeath());
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
     * @return (ARPGCollectableAreaEntity) The collectable item to drop at death. Null by default.
     */
    protected ARPGCollectableAreaEntity getItemToDropAtDeath() {
        return null;
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
            wasHurt = true;

            if (hp <= 0) {
                die();
            }
        }
    }
    
    /**
     * The actions to be executed when the Monster dies.
     */
    protected void die() {
        monsterState = MonsterState.VANISHING;

    }
    
    /**
     *
     * @return (State) The current state of the Monster
     */
    protected MonsterState getMonsterState() {
        return monsterState;
    }
    
    /**
     * @return (boolean) a boolean indicating if the current Monster is alive
     */
    final protected boolean isAlive() {
        return monsterState == MonsterState.ALIVE;
    }
    
    /**
     *
     * @param vulnerability (Vulnerability) The vulnerability to check
     * @return (boolean) a boolean indicating if the monster is vulnerable to the given vulnerability
     */
    private boolean isVulnerableTo(Vulnerability vulnerability) {
        return Arrays.asList(vulnerabilities).contains(vulnerability);
    }

    /**
     * Randomly orientate the Monster
     */
    protected void randomlyOrientate() {
        int randomIndex = RandomGenerator.getInstance().nextInt(4);
        orientate(Orientation.fromInt(randomIndex));
    }

    /**
     * Randomly move the Monster
     */
    protected void randomlyMove(int animationDuration) {
        if (!isDisplacementOccurs()) {
            if (RandomGenerator.getInstance().nextFloat() < PROBABILITY_TO_CHANGE_ORIENTATION) {
                randomlyOrientate();
            }

            move(animationDuration);
        }
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
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
    // MARK:- Interactor
    
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
