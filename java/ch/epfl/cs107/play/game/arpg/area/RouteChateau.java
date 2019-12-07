package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.terrain.CastleDoor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteChateau extends ARPGArea {

    private String[] areaKeys = {"zelda/Route"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 18)
    };
    private Orientation[] orientations = {
            Orientation.DOWN
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(9, 0)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(10,0)}
    };

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        registerActor(new CastleDoor("zelda/Chateau",
                new DiscreteCoordinates(7, 1),
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(9, 13),
                new DiscreteCoordinates(10, 13)));
    }

    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }
    
}
