package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class MagicWaterPojectile extends Projectile{

    private static final float DAMAGE = 1f;

    private static MagicWaterPojectileHandler handler;

    // MARK:- Animation
    private Animation movingAnimation;
    private static final float SIZE = 1f;
    private static final int MOVING_ANIMATION_DURATION = 1;
    private static final String IMG_NAME = "zelda/magicWaterProjectile";

    private static class MagicWaterPojectileHandler implements ARPGInteractionVisitor{

        @Override
        public void interactWith(Monster monster) {
            monster.harm(Monster.Vulnerability.MAGIC, DAMAGE);
        }

        @Override
        public void interactWith(FireSpell fireSpell) {
            fireSpell.extinguish();
        }
    }

    /**
     * Default AreaEntity constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position        (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param speed           (float) speed in cell/Second
     * @param maximumDistance (float) maximum distance in cell
     */
    public MagicWaterPojectile(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position, speed, maximumDistance);

        handler = new MagicWaterPojectileHandler();

        Sprite[] movingSprites  = new Sprite[4];
        for (int i = 0; i < movingSprites.length; ++i) {
            movingSprites[i] = new RPGSprite(IMG_NAME, SIZE, SIZE, this, new RegionOfInterest(32*i,0,32,32));
        }
        movingAnimation = new Animation(MOVING_ANIMATION_DURATION, movingSprites, true);

    }

    @Override
    public void draw(Canvas canvas) {
        movingAnimation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        movingAnimation.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }
}
