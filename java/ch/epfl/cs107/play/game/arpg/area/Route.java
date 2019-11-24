package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Route extends ARPGArea {
    
    private String[] areaKeys = {"zelda/Ferme", "zelda/Village"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(18, 15),
            new DiscreteCoordinates(29, 18)
    };
    private Orientation[] orientations = {
            Orientation.UP,
            Orientation.DOWN
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(0, 15),
            new DiscreteCoordinates(9, 0)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(0, 16)},
            {new DiscreteCoordinates(10, 0)}
    };
    
    @Override
    public String getTitle() {
        return "zelda/Route";
    }
    
    @Override
    protected void createArea() {
        // Background & Foreground
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
    
}
