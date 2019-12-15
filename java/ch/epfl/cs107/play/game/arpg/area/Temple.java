package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Staff;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Temple extends ARPGArea {

    private final String[] areaKeys = {"zelda/RouteTemple"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(5, 5),
    };
    private final Orientation[] orientations = {
            Orientation.DOWN,
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(4, 0)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {}
    };

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // Staff
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(4, 3)));

        // LogMonsters
        registerActor(new LogMonster(this, new DiscreteCoordinates(1,1)));
        registerActor(new LogMonster(this, new DiscreteCoordinates(1,4)));
        registerActor(new LogMonster(this, new DiscreteCoordinates(6,4)));
        registerActor(new LogMonster(this, new DiscreteCoordinates(6,1)));

    }

    @Override
    public String getTitle() {
        return "zelda/Temple";

    }
}
