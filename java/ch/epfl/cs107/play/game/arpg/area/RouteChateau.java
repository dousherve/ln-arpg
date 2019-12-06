package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class RouteChateau extends ARPGArea {

    private String[] areaKeys = {"zelda/Route","zelda/Chateau"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 18),
            new DiscreteCoordinates(7, 1)
    };
    private Orientation[] orientations = {
            Orientation.DOWN,
            Orientation.UP
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(9,0),
            new DiscreteCoordinates(9,13)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(10,0)},
            {new DiscreteCoordinates(10,13)}
    };



    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);
    }

    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }
}
