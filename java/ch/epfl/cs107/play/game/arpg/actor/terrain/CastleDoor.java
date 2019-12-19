package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
    
    private static final String OPEN_IMAGE_NAME = "zelda/castleDoor.open";
    private static final String CLOSE_IMAGE_NAME = "zelda/castleDoor.close";
    
    private Sprite sprite;

    /**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation, DiscreteCoordinates position) {
        // We pass an empty array for the otherCells to the constructor below
        this(destination, otherSideCoordinates, area, orientation, position, new DiscreteCoordinates[0]);
    }

    /**
     * Complementary CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param area        (Area): Owner area, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param otherCells (DiscreteCoordinates...): Other cells occupied by the AreaEntity if any. Assume absolute coordinates, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates ... otherCells) {
        super(destination, otherSideCoordinates, Logic.FALSE, area, orientation, position, otherCells);
    
        sprite = new RPGSprite(
                CLOSE_IMAGE_NAME, 2f, 2f, this,
                new RegionOfInterest(0, 0, 32, 32)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        sprite.draw(canvas);
    }
    
    /**
     * Open the door.
     */
    public void open() {
        setSignal(Logic.TRUE);
    }
    
    /**
     * Close the door.
     */
    public void close() {
        setSignal(Logic.FALSE);
    }
    
    @Override
    protected void setSignal(Logic signal) {
        super.setSignal(signal);
     
        sprite.setName(ResourcePath.getSprite(isOpen() ? OPEN_IMAGE_NAME : CLOSE_IMAGE_NAME));
    }
    
    @Override
    public boolean takeCellSpace() {
        return !isOpen();
    }
    
    @Override
    public boolean isViewInteractable() {
        return !isOpen();
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
}
