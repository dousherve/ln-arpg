package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Staff;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Rock;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Temple extends ARPGArea {

    private List<LogMonster> monsters = new ArrayList<>();
    private Rock rock;

    boolean doorOpen;
    boolean noMoreMonsters;

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
    public void update(float deltaTime) {
        super.update(deltaTime);

        noMoreMonsters = true;
        for (LogMonster logMonster : monsters) {
            if (logMonster.isAlive()) {
                noMoreMonsters = false;
                break;
            }
        }

        if (noMoreMonsters && !doorOpen){
            rock.crack();
            doorOpen = true;
        }
    }

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // Staff
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(4, 3)));

        doorOpen = false;
        rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(4, 0));
        registerActor(rock);

        // LogMonsters
        monsters.add(new LogMonster(this, new DiscreteCoordinates(1,1)));
        monsters.add(new LogMonster(this, new DiscreteCoordinates(1,4)));
        monsters.add(new LogMonster(this, new DiscreteCoordinates(6,4)));
        monsters.add(new LogMonster(this, new DiscreteCoordinates(6,1)));

        for (Monster monster : monsters) {
            registerActor(monster);
        }

    }

    @Override
    public String getTitle() {
        return "zelda/Temple";

    }
}
