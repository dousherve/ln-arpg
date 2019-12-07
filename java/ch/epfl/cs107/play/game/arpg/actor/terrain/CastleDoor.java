package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
    
    private static final String OPEN_IMAGE = "zelda/castleDoor.open";
    private static final String CLOSE_IMAGE = "zelda/castleDoor.open";
    
    private Sprite sprite;

    /**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        this(destination, otherSideCoordinates, signal, area, orientation, position,
                new DiscreteCoordinates[0]);
        // We pass an empty array for the otherCells, and we call the constructor below
    }

    /**
     * Complementary CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param otherCells (DiscreteCoordinates...): Other cells occupied by the AreaEntity if any. Assume absolute coordinates, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates ... otherCells){
        super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);
        
        sprite = new RPGSprite(OPEN_IMAGE, 2f, 2f, this,
                new RegionOfInterest(0, 0, 32, 32));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        sprite.draw(canvas);
    }
    
}
