package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.character.Character;
import ch.epfl.cs107.play.game.arpg.actor.character.Guard;
import ch.epfl.cs107.play.game.arpg.actor.character.Seller;
import ch.epfl.cs107.play.game.arpg.actor.terrain.CaveOpening;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Village extends ARPGArea {
    
    private final String[] areaKeys = {"zelda/Ferme", "zelda/Ferme", "zelda/Route", "Grotte"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(4, 1),
            new DiscreteCoordinates(14, 1),
            new DiscreteCoordinates(9, 1),
            new DiscreteCoordinates(16, 1)
    };
    private final Orientation[] orientations = {
            Orientation.UP,
            Orientation.UP,
            Orientation.UP,
            Orientation.UP
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(4, 19),
            new DiscreteCoordinates(13, 19),
            new DiscreteCoordinates(29, 19),
            new DiscreteCoordinates(25, 18)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(5, 19)},
            {new DiscreteCoordinates(14, 19), new DiscreteCoordinates(15, 19)},
            {new DiscreteCoordinates(30, 19)},
            {}
    };
    
    
    @Override
    public String getTitle() {
        return "zelda/Village";
    }
    
    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();


        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        registerActor(new CaveOpening(this, Orientation.DOWN, new DiscreteCoordinates(25, 18)));

        //  Characters
        registerActor(new Character(this, Orientation.DOWN, new DiscreteCoordinates(5,5)));
        registerActor(new Guard(this, Orientation.DOWN, new DiscreteCoordinates(15,6)));
        registerActor(new Character(this, Orientation.DOWN, new DiscreteCoordinates(30,15)));
        registerActor(new Seller(this, Orientation.DOWN, new DiscreteCoordinates(17, 9)));
    }
    
}
