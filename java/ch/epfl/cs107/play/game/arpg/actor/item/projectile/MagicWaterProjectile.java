package ch.epfl.cs107.play.game.arpg.actor.item.projectile;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.attacker.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.attacker.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class MagicWaterProjectile extends Projectile {
    
    private class MagicWaterProjectileHandler implements ARPGInteractionVisitor{
        
        @Override
        public void interactWith(Monster monster) {
            if (canInteractWith(monster)) {
                monster.harm(Monster.Vulnerability.MAGIC, DAMAGE);
            }
        }
        
        @Override
        public void interactWith(FireSpell fireSpell) {
            fireSpell.extinguish();
        }
        
    }

    private static final float DAMAGE = 2.5f;

    private static MagicWaterProjectileHandler handler;

    // MARK:- Animation
    private Animation movingAnimation;
    private static final float SIZE = 1f;
    private static final int MOVING_ANIMATION_DURATION = 1;
    private static final String IMG_NAME = "zelda/magicWaterProjectile";

    /**
     * Default AreaEntity constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position        (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param speed           (float) speed
     * @param maximumDistance (float) maximum distance
     */
    public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position, speed, maximumDistance);

        handler = new MagicWaterProjectileHandler();

        Sprite[] movingSprites  = new Sprite[4];
        for (int i = 0; i < movingSprites.length; ++i) {
            movingSprites[i] = new RPGSprite(
                    IMG_NAME, SIZE, SIZE, this,
                    new RegionOfInterest(32 * i, 0, 32, 32)
            );
        }
        
        movingAnimation = new Animation(MOVING_ANIMATION_DURATION, movingSprites);
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        movingAnimation.update(deltaTime);
    }
    
    @Override
    public void draw(Canvas canvas) {
        movingAnimation.draw(canvas);
    }
    
    // MARK:- Interactable

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    // MARK:- Interactor

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
}
