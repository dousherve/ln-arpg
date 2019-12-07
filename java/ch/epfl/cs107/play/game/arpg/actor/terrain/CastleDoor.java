package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Arrays;

public class CastleDoor extends Door {

    private Sprite sprite;

    private String openImage = "zelda/castleDoor.open";
    private String closeImage = "zelda/castleDoor.open";


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
        super(destination, otherSideCoordinates, signal, area, orientation, position);

        sprite = new RPGSprite(openImage, 2f,2f, this,
                new RegionOfInterest(0,0,32,32));

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
        this(destination, otherSideCoordinates, signal, area, orientation, position);
        this.currentCells.addAll(Arrays.asList(otherCells));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        sprite.draw(canvas);

    }
}
