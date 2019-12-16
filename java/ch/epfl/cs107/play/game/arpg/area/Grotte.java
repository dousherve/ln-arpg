package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Sword;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Rock;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Grotte extends ARPGArea {

    private final String[] areaKeys = {"zelda/Village"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(25, 17)
    };
    private final Orientation[] orientations = {
            Orientation.DOWN
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(16, 0)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {}
    };

    // Coordinates for rocks
    private final DiscreteCoordinates[] rockCoordinates = {
            new DiscreteCoordinates(13,5),
            new DiscreteCoordinates(3,3),
            new DiscreteCoordinates(10,7),
            new DiscreteCoordinates(8,2),
            new DiscreteCoordinates(2,12),
            new DiscreteCoordinates(10,14),
            new DiscreteCoordinates(11,14),
            new DiscreteCoordinates(4,22),
            new DiscreteCoordinates(23,9)
    };

    @Override
    protected void createArea() {

        // Background
        registerActor(new Background(this));

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // Sword
        registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(22, 36)));

        // Rocks
        for (DiscreteCoordinates rockCoordinate : rockCoordinates) {
            registerActor(new Rock(this, Orientation.DOWN, rockCoordinate));
        }
    }

    @Override
    public String getTitle() {
        return "Grotte";
    }
}
