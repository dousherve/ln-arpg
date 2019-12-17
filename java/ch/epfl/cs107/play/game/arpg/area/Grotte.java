package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Sword;
import ch.epfl.cs107.play.game.arpg.actor.monster.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Rock;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

public class Grotte extends ARPGArea {

    // MARK:- FlameSkull spawner
    private static final float MIN_TIME_SPAWN = 5f;
    private static final float MAX_TIME_SPAWN = 7f;
    private static final DiscreteCoordinates SPAWNER_COORDINATES_1 = new DiscreteCoordinates(16, 29);
    private static final DiscreteCoordinates SPAWNER_COORDINATES_2 = new DiscreteCoordinates(2, 14);
    private float timerSpawner = 0;

    // MARK:- Doors
    private final String[] areaKeys = {"zelda/Village", "GrotteMew"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(25, 17),
            new DiscreteCoordinates(8,3)
    };
    private final Orientation[] orientations = {
            Orientation.DOWN,
            Orientation.UP,
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(16, 0),
            new DiscreteCoordinates(6, 32)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {}, {}
    };

    // Coordinates for rocks
    private final DiscreteCoordinates[] rockCoordinates = {
            new DiscreteCoordinates(13,5),
            new DiscreteCoordinates(3,3),
            new DiscreteCoordinates(10,7),
            new DiscreteCoordinates(8,2),
            new DiscreteCoordinates(2,12),
            new DiscreteCoordinates(10,14),
            new DiscreteCoordinates(11,14),
            new DiscreteCoordinates(4,22),
            new DiscreteCoordinates(23,9)
    };

    @Override
    protected void createArea() {

        // Background
        registerActor(new Background(this));

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // Sword
        registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(22, 36)));

        // Rocks
        for (DiscreteCoordinates rockCoordinate : rockCoordinates) {
            registerActor(new Rock(this, Orientation.DOWN, rockCoordinate));
        }
    }

    @Override
    public String getTitle() {
        return "Grotte";
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timerSpawner = Math.max(timerSpawner - deltaTime, 0);

        if (timerSpawner <= 0){
            registerActor(new FlameSkull(this, Orientation.DOWN, SPAWNER_COORDINATES_1));
            registerActor(new FlameSkull(this, Orientation.RIGHT, SPAWNER_COORDINATES_2));
            timerSpawner = MIN_TIME_SPAWN + (MAX_TIME_SPAWN - MIN_TIME_SPAWN) * RandomGenerator.getInstance().nextFloat();
        }

    }

}
