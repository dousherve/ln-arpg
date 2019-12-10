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
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collections;
import java.util.List;

public class FireSpell extends AreaEntity implements Interactor {
    
    private static class FireSpellHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(ARPGPlayer player) {
            player.harm(DAMAGE);
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
            monster.harm(Monster.Vulnerability.FIRE, DAMAGE);
        }
        
    }

    /// Lifetime in simulation frames
    private float lifetime;
    
    /// Interaction handler
    private FireSpellHandler handler;

    private static final float MIN_LIFE_TIME = 5f, MAX_LIFE_TIME = 10f;
    
    private static final int PROPAGATION_TIME_FIRE = 100;
    
    private int cycleCount;

    private static final float SIZE = 1f;

    private static final float DAMAGE = 0.5f;

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
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int strength) {
        super(area, orientation, position);
        
        handler = new FireSpellHandler();
        
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

        randomlyOrientate();
    }

    @Override
    public void update(float deltaTime) {
        lifetime -= deltaTime;
        if (lifetime <= 0) {
            lifetime = 0;
            getOwnerArea().unregisterActor(this);
            animation.reset();
            return;
        }
        
        ++cycleCount;
        if (cycleCount >= PROPAGATION_TIME_FIRE) {
            cycleCount = 0;
            if (strength > 0) {
                DiscreteCoordinates newFlamePos = getCurrentMainCellCoordinates().jump(
                        getOrientation().toVector()
                );
                FireSpell flame = new FireSpell(getOwnerArea(), getOrientation(),
                        newFlamePos, strength - 1);
                if (getOwnerArea().canEnterAreaCells(
                        flame, Collections.singletonList(newFlamePos))
                ) {
                    getOwnerArea().registerActor(flame);
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
        return;
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
