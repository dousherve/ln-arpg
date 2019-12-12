package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

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
        
        // TODO: debug
        registerActor(new FireSpell(this, Orientation.DOWN, new DiscreteCoordinates(5, 9), 1));
        registerActor(new Arrow(this, Orientation.DOWN, new DiscreteCoordinates(6,9), 1,5));
    }
    
}
