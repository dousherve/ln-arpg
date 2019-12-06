package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.Heart;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Ferme extends ARPGArea {
    
    private String[] areaKeys = {"zelda/Route", "zelda/Village", "zelda/Village"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(1, 15),
            new DiscreteCoordinates(4, 18),
            new DiscreteCoordinates(14, 18)
    };
    private Orientation[] orientations = {
            Orientation.RIGHT,
            Orientation.DOWN,
            Orientation.DOWN
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(19, 15),
            new DiscreteCoordinates(4, 0),
            new DiscreteCoordinates(13, 0)
    };
    private DiscreteCoordinates[][] otherCells = {
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
        
        registerActor(new Coin(this, Orientation.DOWN, new DiscreteCoordinates(7, 7)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 7)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 8)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 9)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 10)));
    }
    
}
