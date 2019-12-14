package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.actor.monster.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.monster.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;

public class RouteChateau extends ARPGArea {

    private final String[] areaKeys = {"zelda/Route"};
    private final DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 18)
    };
    private final Orientation[] orientations = {
            Orientation.DOWN
    };
    private final DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(9, 0)
    };
    private final DiscreteCoordinates[][] otherCells = {
            {new DiscreteCoordinates(10, 0)}
    };

    @Override
    protected void createArea() {
        // Background & Foreground
        registerBackgroundAndForeground();

        // Doors
        registerDoors(areaKeys, destinationCoords, orientations, positions, otherCells);

        // CastleDoor
        registerActor(new CastleDoor("zelda/Chateau",
                new DiscreteCoordinates(7, 1),
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(9, 13),
                new DiscreteCoordinates(10, 13)));
        
        // Left Grass
        for (int i = 5; i <= 6; ++i) {
            for (int j = 6; j <= 8; ++j) {
                registerActor(new Grass(this, new DiscreteCoordinates(i, j)));
            }
        }
    
        // Right Grass
        for (int i = 13; i <= 14; ++i) {
            for (int j = 9; j <= 11; ++j) {
                registerActor(new Grass(this, new DiscreteCoordinates(i, j)));
            }
        }
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // DEBUG:- spawn a DarkLord monster
        if (getKeyboard().get(Keyboard.D).isPressed()) {
            registerActor(new DarkLord(this, Orientation.DOWN,  new DiscreteCoordinates(8, 10)));
        }

        // DEBUG:- spawn a FlameSkull monster
        if (getKeyboard().get(Keyboard.S).isPressed()) {
            registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(8, 10)));
        }

        // DEBUG:- spawn a LogMonster monster
        if (getKeyboard().get(Keyboard.L).isPressed()) {
            registerActor(new LogMonster(this, new DiscreteCoordinates(9, 9)));
        }

        // DEBUG:- spawn a Bomb
        if (getKeyboard().get(Keyboard.B).isPressed()) {
            registerActor(new Bomb(this, new DiscreteCoordinates(8, 7)));
        }
    }
    
    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }
    
}
