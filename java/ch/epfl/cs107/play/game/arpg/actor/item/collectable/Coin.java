package ch.epfl.cs107.play.game.arpg.actor.item.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Coin extends ARPGCollectableAreaEntity {
    
    /// The amount of money the coin represents
    private final static int VALUE = 10;
    
    private Animation animation;
    private static final int ANIMATION_DURATION = 4;
    
    /// The size of the Coin
    private static final float SIZE = 1f;
    
    /**
     * Default Coin constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    
        Sprite[] sprites = new RPGSprite[4];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new RPGSprite(
                    "zelda/coin", SIZE, SIZE, this,
                    new RegionOfInterest(16 * i, 0, 16, 16)
            );
        }
        
        animation = new Animation(ANIMATION_DURATION, sprites, true);
    }
    
    /**
     * Return the amount of money given by the Coin
     * @return (int)
     */
    public int getValue() {
        return VALUE;
    }
    
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }
    
    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }
    
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
