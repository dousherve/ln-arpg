package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Chateau extends ARPGArea {

    private String[] areaKeys = {"zelda/RouteChateau"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 12)
    };
    private Orientation[] orientations = {
            Orientation.DOWN,
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(7,0)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(8,0)}
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
        return "zelda/Chateau";
    }
}
