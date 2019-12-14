package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.character.King;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Chateau extends ARPGArea {

    private final String[] areaKeys = {"zelda/RouteChateau"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 12)
    };
    private final Orientation[] orientations = {
            Orientation.DOWN,
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(7,0)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(8,0)}
    };

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        //King
        registerActor(new King(this, Orientation.DOWN, new DiscreteCoordinates(7, 12)));

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);
    }

    @Override
    public String getTitle() {
        return "zelda/Chateau";
    }
    
}
