package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Waterfall extends AreaEntity {
    
    private Animation animation;
    private static final int ANIMATION_DURATION = 2;
    
    /**
     * Default Waterfall constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Waterfall(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        
        Sprite[] sprites = new Sprite[3];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new RPGSprite("zelda/waterfall", 4, 4,
                    this, new RegionOfInterest(64 * i, 0, 64, 64),
                    new Vector(4, 0)
            );
        }
        
        animation = new Animation(ANIMATION_DURATION, sprites);
    }
    
    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }
    
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public boolean takeCellSpace() {
        return true;
    }
    
    @Override
    public boolean isCellInteractable() {
        return false;
    }
    
    @Override
    public boolean isViewInteractable() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}
    
}
