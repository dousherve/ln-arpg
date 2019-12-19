package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Chest;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Grotte2 extends ARPGArea {

    // MARK:- Doors
    private final String[] areaKeys = {"Grotte"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(6,31)
    };
    private final Orientation[] orientations = {
            Orientation.DOWN
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(8, 2),
    };
    private final DiscreteCoordinates[][] otherCells = {
            {}
    };

    @Override
    protected void createArea() {
        // Background
        registerActor(new Background(this));

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // Chest
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(8,7)));
    }

    @Override
    public String getTitle() {
        return "GrotteMew";
    }
    
}
