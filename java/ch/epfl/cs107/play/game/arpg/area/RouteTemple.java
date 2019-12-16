package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.character.Woman;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class RouteTemple extends ARPGArea {

    private final String[] areaKeys = {"zelda/Route", "zelda/Temple"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(18, 10),
            new DiscreteCoordinates(4, 1)
    };
    private final Orientation[] orientations = {
            Orientation.LEFT,
            Orientation.UP
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(0, 4),
            new DiscreteCoordinates(5, 6)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(0, 5)},
            {}
    };

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        registerActor(new Woman(this, Orientation.DOWN, new DiscreteCoordinates(4, 5)));
    }

    @Override
    public String getTitle() {
        return "zelda/RouteTemple";
    }
}
