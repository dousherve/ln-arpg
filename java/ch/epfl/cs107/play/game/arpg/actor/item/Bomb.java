package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {
    
    private class BombHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(Grass grass) {
            if (isExploding) {
                grass.cut();
            }
        }
        
    }
    
    private final BombHandler handler;
    
    private Sprite sprite;
    private Animation explosionAnimation;
    private static final int ANIMATION_DURATION = 4;
    
    /// The remaining time before the Bomb explodes
    private int timer;
    /// Keeps track of the current state of the Bomb
    private boolean isExploding, hasExploded;
    
    /**
     * Default Bomb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        
        handler = new BombHandler();
    
        sprite = new RPGSprite("zelda/bomb", 1, 1, this,
                new RegionOfInterest(0, 0, 16, 16));
    
        Sprite[] explosionSprites = new Sprite[7];
        for (int i = 0; i < explosionSprites.length; ++i) {
            explosionSprites[i] = new Sprite("zelda/explosion", 2, 2, this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0));
        }
        explosionAnimation = new Animation(ANIMATION_DURATION / 2, explosionSprites, false);
    
        timer = 75;
        hasExploded = false;
        isExploding = false;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (!isExploding && !hasExploded) {
            sprite.draw(canvas);
        }
        
        if (isExploding && !explosionAnimation.isCompleted()) {
            explosionAnimation.draw(canvas);
        }
    }
    
    @Override
    public void update(float deltaTime) {
        timer = Math.max(0, timer - 1);
        
        if (timer == 0 && !isExploding && !hasExploded) {
            isExploding = true;
            // TODO: remove the BOOMS sysout
            System.out.println("BOOMS");
        }
        
        if (isExploding) {
            explosionAnimation.update(deltaTime);
            if (explosionAnimation.isCompleted()) {
                isExploding = false;
                hasExploded = true;
            }
        }
    }
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    // MARK:- Interactor
    
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }
    
    @Override
    public boolean wantsCellInteraction() {
        return isExploding;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return isExploding;
    }
    
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
    // MARK:- Interactable
    
    @Override
    public boolean takeCellSpace() {
        return false;
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
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
