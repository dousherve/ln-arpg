package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Chest extends AreaEntity {
    
    private static int WIDTH = 32, HEIGHT = 48;

    // MARK:- Animation
    private Sprite spriteOpen;
    private Sprite spriteClose;
    private Animation animation;
    private static final int ANIMATION_DURATION = 4;

    private boolean isOpen;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Chest(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        
        isOpen = false;

        spriteClose = new RPGSprite(
                "zelda/chests", 1f, 2f, this,
                new RegionOfInterest(0, 0, WIDTH, HEIGHT)
        );
        spriteClose.setDepth(50);
        
        spriteOpen = new RPGSprite(
                "zelda/chests", 1f, 2f, this,
                new RegionOfInterest(0, HEIGHT * 3, WIDTH, HEIGHT)
        );
        spriteOpen.setDepth(50);

        Sprite[] sprites = new Sprite[4];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new Sprite("zelda/chests", 1f, 2f,
                    this,
                    new RegionOfInterest(0, i * HEIGHT, WIDTH, HEIGHT)
            );
        }
        animation = new Animation(ANIMATION_DURATION / 2, sprites, false);
    }

    /**
     * Open the chest
     */
    public void open() {
        generateCollectableItem();
        isOpen = true;
    }

    /**
     * Spawn a collectable Item (Coin)
     */
    private void generateCollectableItem() {
        if (!isOpen) {
            for (Orientation orientation : Orientation.values()) {
                getOwnerArea().registerActor(
                        new Coin(
                                getOwnerArea(), Orientation.DOWN,
                                getCurrentMainCellCoordinates().jump(orientation.toVector())
                        )
                );
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOpen) {
            if (animation.isCompleted()) {
                spriteOpen.draw(canvas);
            } else {
                animation.draw(canvas);
            }
        } else {
            spriteClose.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (isOpen) {
            animation.update(deltaTime);
        }
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
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
