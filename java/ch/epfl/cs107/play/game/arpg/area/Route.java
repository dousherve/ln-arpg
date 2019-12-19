package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.character.Alice;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Waterfall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Route extends ARPGArea {
    
    private final String[] areaKeys = {"zelda/Ferme", "zelda/Village", "zelda/RouteChateau", "zelda/RouteTemple"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(18, 15),
            new DiscreteCoordinates(29, 18),
            new DiscreteCoordinates(9,1),
            new DiscreteCoordinates(1, 4)
    };
    private final Orientation[] orientations = {
            Orientation.UP,
            Orientation.DOWN,
            Orientation.UP,
            Orientation.RIGHT
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(0, 15),
            new DiscreteCoordinates(9, 0),
            new DiscreteCoordinates(9,  19),
            new DiscreteCoordinates(19, 10)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(0, 16)},
            {new DiscreteCoordinates(10, 0)},
            {new DiscreteCoordinates(10,19)},
            {}
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
        
        // Waterfall
        registerActor(new Waterfall(this, new DiscreteCoordinates(15, 3)));

        // Bridge
        registerActor(new Bridge(this, Orientation.DOWN, new DiscreteCoordinates(15, 9)));

        // Alice
        registerActor(new Alice(this, Orientation.LEFT, new DiscreteCoordinates(15,10)));
        
        // Two monsters
        registerActor(new LogMonster(this, new DiscreteCoordinates(9,11)));
        registerActor(new LogMonster(this, new DiscreteCoordinates(3,5)));
    }
    
}
