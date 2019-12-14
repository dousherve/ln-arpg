package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class FireSpell extends AreaEntity implements Interactor {
    
    private class FireSpellHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(ARPGPlayer player) {
            if (attackHandler.canHarm(player)) {
                player.harm(DAMAGE);
            }
        }
    
        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }
    
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }
    
        @Override
        public void interactWith(Monster monster) {
            if (attackHandler.canHarm(monster)) {
                monster.harm(Monster.Vulnerability.FIRE, DAMAGE);
            }
        }
        
    }

    /// Lifetime in simulation frames
    private float lifetime;
    
    /// Interaction handler
    private final FireSpellHandler handler;
    
    /// Continuous attack handler
    private final ContinuousAttackHandler attackHandler;

    private static final float MIN_LIFE_TIME = 5f, MAX_LIFE_TIME = 10f;

    private static final int LINEAR_PROPAGATION_TIME = 10;
    private static final int NORMAL_PROPAGATION_TIME = 100;

    private int propagationTime;

    private boolean linearPropagation;
    
    private int cycleCount;

    private static final float SIZE = 1f;

    /// The damage to inflict
    private static final float DAMAGE = 0.5f;
    
    private static final float ATTACK_DELAY = 0.3f;

    /// The strength
    private int strength;

    // MARK:- Animation:
    private Animation animation;
    private static final int ANIMATION_DURATION = 2;

    /**
     * Default FireSpell constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param strength    (int) The strength of the spell
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int strength, boolean linearPropagation) {
        super(area, orientation, position);
        
        handler = new FireSpellHandler();
        attackHandler = new ContinuousAttackHandler(ATTACK_DELAY);

        this.linearPropagation = linearPropagation;

        propagationTime = linearPropagation ? LINEAR_PROPAGATION_TIME : NORMAL_PROPAGATION_TIME;

        cycleCount = 0;
        
        lifetime = MIN_LIFE_TIME +
                (MAX_LIFE_TIME - MIN_LIFE_TIME) * RandomGenerator.getInstance().nextFloat();

        Sprite[] sprites = new Sprite[7];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new RPGSprite("zelda/fire", SIZE, SIZE, this,
                    new RegionOfInterest(16 * i, 0, 16, 16),
                    new Vector(0,0));
        }
        animation = new Animation(ANIMATION_DURATION, sprites, true);

        this.strength = Math.max(strength, 0);

        if(!this.linearPropagation) {
            randomlyOrientate();
        }
    }

    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int strength){
        this(area, orientation, position, strength, false);
    }

    /**
     * extinguish the fire
     */
    public void extinguish(){
        getOwnerArea().unregisterActor(this);
        animation.reset();
    }

    @Override
    public void update(float deltaTime) {
        lifetime -= deltaTime;
        if (lifetime <= 0) {
            lifetime = 0;
            extinguish();
            return;
        }
        
        // Update the continuous attack handler
        attackHandler.update(deltaTime);
        
        ++cycleCount;
        if (cycleCount >= propagationTime) {
            cycleCount = 0;
            if (strength > 0) {
                DiscreteCoordinates newFlamePos = getCurrentMainCellCoordinates().jump(
                        getOrientation().toVector()
                );

                FireSpell flame = new FireSpell(getOwnerArea(), getOrientation(),
                        newFlamePos, strength - 1, linearPropagation);

                if (getOwnerArea().canEnterAreaCells(
                        flame, Collections.singletonList(newFlamePos))
                ) {
                    getOwnerArea().registerActor(flame);
                }

                if(linearPropagation){
                    strength = 0;
                }

            }
        }
        
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Randomly orientate the Flame
     */
    private void randomlyOrientate() {
        int randomIndex = RandomGenerator.getInstance().nextInt(4);
        orientate(Orientation.fromInt(randomIndex));
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
    
    // MARK:- Interactor
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(
                getCurrentMainCellCoordinates().jump(getOrientation().toVector())
        );
    }
    
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }
    
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
}
