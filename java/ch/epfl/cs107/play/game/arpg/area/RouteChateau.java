package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
import ch.epfl.cs107.play.game.arpg.actor.monster.FlameSkull;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;

public class RouteChateau extends ARPGArea {

    private String[] areaKeys = {"zelda/Route"};
    private DiscreteCoordinates[] destinationCoords = {
            new DiscreteCoordinates(9, 18)
    };
    private Orientation[] orientations = {
            Orientation.DOWN
    };
    private DiscreteCoordinates[] positions = {
            new DiscreteCoordinates(9, 0)
    };
    private DiscreteCoordinates[][] otherCells = {
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
        
        // TODO: see if we must remove these key actions
        if (getKeyboard().get(Keyboard.S).isPressed()) {
            registerActor(new FlameSkull(this, new DiscreteCoordinates(8, 10)));
        }
    
        if (getKeyboard().get(Keyboard.B).isPressed()) {
            registerActor(new Bomb(this, new DiscreteCoordinates(8, 7)));
        }
    }
    
    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }
    
}
