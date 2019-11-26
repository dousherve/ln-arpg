package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Grass extends AreaEntity {
    
    private Sprite sprite;
    private Animation cutAnimation;
    private static final int ANIMATION_DURATION = 4;
    
    /// Keeps track of the current state of the Grass
    private boolean isCut;
    
    /**
     * Default Grass constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Grass(Area area, DiscreteCoordinates position) {
        // TODO: see if there is a way not to take into account the orientation, because it's pointless here
        super(area, Orientation.DOWN, position);
        
        sprite = new RPGSprite("zelda/grass", 1, 1, this);
        
        Sprite[] cutSprites = new Sprite[4];
        for (int i = 0; i < cutSprites.length; ++i) {
            cutSprites[i] = new Sprite("zelda/grass.sliced", 2, 2, this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0));
        }
        cutAnimation = new Animation(ANIMATION_DURATION / 2, cutSprites, false);
        
        isCut = false;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (!isCut) {
            sprite.draw(canvas);
        }
        
        if (isCut && !cutAnimation.isCompleted()) {
            cutAnimation.draw(canvas);
        }
    }
    
    @Override
    public void update(float deltaTime) {
        if (isCut) {
            if (cutAnimation.isCompleted()) {
                // Unregister if the slicing animation after the cut is done
                getOwnerArea().unregisterActor(this);
            } else {
                // If not, animate
                cutAnimation.update(deltaTime);
            }
        }
    }
    
    // MARK:- Specific Grass method
    
    /**
     * Cut the current Grass
     */
    public void cut() {
        isCut = true;
    }
    
    // MARK:- Interactable
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public boolean takeCellSpace() {
        return !isCut;
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
    
}
