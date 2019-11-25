package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Village extends ARPGArea {
    
    private String[] areaKeys = {"zelda/Ferme", "zelda/Ferme", "zelda/Route"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(4, 1),
            new DiscreteCoordinates(14, 11),
            new DiscreteCoordinates(9, 1)
    };
    private Orientation[] orientations = {
            Orientation.UP,
            Orientation.UP,
            Orientation.UP
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(4, 19),
            new DiscreteCoordinates(13, 19),
            new DiscreteCoordinates(29, 19)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(5, 19)},
            {new DiscreteCoordinates(14, 19), new DiscreteCoordinates(15, 19)},
            {new DiscreteCoordinates(30, 19)}
    };
    
    
    @Override
    public String getTitle() {
        return "zelda/Village";
    }
    
    @Override
    protected void createArea() {
        // Background & Foreground
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        
        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);
    }
    
}
