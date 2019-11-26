package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Grass extends AreaEntity {
    
    private Sprite sprite;
    private Animation cutAnimation;
    private static final int ANIMATION_DURATION = 30;
    
    /// Keeps track of the current state
    private boolean isCut;
    
    /**
     * Default Grass constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Grass(Area area, DiscreteCoordinates position) {
        // TODO: see if there is a way to tell the orientation to go and fuck his ancestors, because pointless here
        super(area, Orientation.DOWN, position);
        
        sprite = new RPGSprite("zelda/grass", 1, 1, this);
        
        Sprite[] cutSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            cutSprites[i] = new Sprite("zelda/grass.sliced", 2, 2, this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0.f));
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
        if (isCut && !cutAnimation.isCompleted()) {
            cutAnimation.update(deltaTime);
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
        ((RPGInteractionVisitor) v).interactWith(this);
    }
    
}
