package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Arrow extends Projectile {

    private static final float DAMAGE = .5f;

    private static ArrowHandler handler;

    // MARK:- Sprite
    private Sprite[] movingSprites;
    private static final float SIZE = 1f;
    private static final String IMG_NAME = "zelda/arrow";


    private static class ArrowHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Monster monster) {
            monster.harm(Monster.Vulnerability.PHYSICAL, DAMAGE);
        }

        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }

        @Override
        public void interactWith(FireSpell fireSpelle) {
            fireSpelle.extinguish();
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
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position, speed, maximumDistance);

        System.out.println(orientation);

        handler = new ArrowHandler();

        movingSprites = new Sprite[4];
        Orientation[] o = new Orientation[]{Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT};
        for (int i = 0; i < movingSprites.length; ++i) {
            movingSprites[o[i].ordinal()] = new Sprite(IMG_NAME, SIZE, SIZE, this, new RegionOfInterest(32*i,0,32,32));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        movingSprites[getOrientation().ordinal()].draw(canvas);
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
