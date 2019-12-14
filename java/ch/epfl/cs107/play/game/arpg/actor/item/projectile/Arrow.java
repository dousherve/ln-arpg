package ch.epfl.cs107.play.game.arpg.actor.item.projectile;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Arrow extends Projectile {
    
    private class ArrowHandler implements ARPGInteractionVisitor {
        
        @Override
        public void interactWith(Monster monster) {
            if (canInteractWith(monster)) {
                monster.harm(Monster.Vulnerability.PHYSICAL, DAMAGE);
            }
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
        public void interactWith(FireSpell fireSpell) {
            fireSpell.extinguish();
        }
        
    }

    private static final float DAMAGE = 1f;

    private final ArrowHandler handler;

    // MARK:- Sprite
    
    private Sprite[] movingSprites;
    private static final float SIZE = 1f;
    private static final String IMG_NAME = "zelda/arrow";

    /**
     * Default Arrow constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position        (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param speed           (float) speed
     * @param maximumDistance (float) maximum distance
     */
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position, speed, maximumDistance);
        
        handler = new ArrowHandler();

        movingSprites = new Sprite[Orientation.values().length];
        for (int i = 0; i < movingSprites.length; ++i) {
            movingSprites[Orientation.fromInt(i).ordinal()] = 
                    new Sprite(
                            IMG_NAME, SIZE, SIZE, this,
                            new RegionOfInterest(32 * i, 0, 32, 32)
                    );
        }
    }

    @Override
    public void draw(Canvas canvas) {
        movingSprites[getOrientation().ordinal()].draw(canvas);
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
