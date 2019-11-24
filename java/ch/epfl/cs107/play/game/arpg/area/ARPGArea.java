package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;


public abstract class ARPGArea extends Area {
    
    private ARPGBehavior behavior;
    
    /**
     * Create the area by adding all of its actors
     * Called by the begin() method
     */
    protected abstract void createArea();
    
    @Override
    public final float getCameraScaleFactor() {
        // TODO: Update this value
        return 10.0f;
    }
    
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            
            behavior = new ARPGBehavior(window, getTitle());
            setBehavior(behavior);
            
            createArea();
            return true;
        }
        
        return false;
    }
    
    protected void registerDoors(String[] areaKeys, DiscreteCoordinates[] destinationCoords, Orientation[] orientations, DiscreteCoordinates[] positions, DiscreteCoordinates[][] otherCells) {
        for (int i = 0; i < areaKeys.length; ++i) {
            registerActor(new Door(
                    areaKeys[i],
                    destinationCoords[i],
                    Logic.TRUE,
                    this,
                    orientations[i],
                    positions[i],
                    otherCells[i])
            );
        }
    }
    
}
