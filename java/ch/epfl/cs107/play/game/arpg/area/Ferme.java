package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.character.Character;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Ferme extends ARPGArea {
    
    private final String[] areaKeys = {"zelda/Route", "zelda/Village", "zelda/Village"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(1, 15),
            new DiscreteCoordinates(4, 18),
            new DiscreteCoordinates(14, 18)
    };
    private final Orientation[] orientations = {
            Orientation.RIGHT,
            Orientation.DOWN,
            Orientation.DOWN
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(19, 15),
            new DiscreteCoordinates(4, 0),
            new DiscreteCoordinates(13, 0)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(19, 16)},
            {new DiscreteCoordinates(5, 0)},
            {new DiscreteCoordinates(14, 0)}
    };
    
    @Override
    public String getTitle() {
        return "zelda/Ferme";
    }
    
    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();
        
        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        registerActor(new Character(this, Orientation.DOWN, new DiscreteCoordinates(5,7)));
    }
    
}
