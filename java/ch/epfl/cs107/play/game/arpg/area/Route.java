package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Route extends ARPGArea {
    
    private String[] areaKeys = {"zelda/Ferme", "zelda/Village", "zelda/RouteChateau"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(18, 15),
            new DiscreteCoordinates(29, 18),
            new DiscreteCoordinates(9,1)
    };
    private Orientation[] orientations = {
            Orientation.UP,
            Orientation.DOWN,
            Orientation.UP
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(0, 15),
            new DiscreteCoordinates(9, 0),
            new DiscreteCoordinates(9,19)
    };
    private DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(0, 16)},
            {new DiscreteCoordinates(10, 0)},
            {new DiscreteCoordinates(10,19)}
    };
    
    @Override
    public String getTitle() {
        return "zelda/Route";
    }
    
    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();
        
        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);
        
        // Grass
        for (int i = 5; i <= 7; ++i) {
            for (int j = 6; j <= 11; ++j) {
                registerActor(new Grass(this, new DiscreteCoordinates(i, j)));
            }
        }
        
        // Bomb
        // TODO: remove debug bomb
        registerActor(new Bomb(this, new DiscreteCoordinates(8, 10)));
    }
    
}
