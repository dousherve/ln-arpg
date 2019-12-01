package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;


public abstract class ARPGArea extends Area {
    
    /**
     * Create the area by adding all of its actors
     * Called by the begin() method
     */
    protected abstract void createArea();
    
    @Override
    public final float getCameraScaleFactor() {
        return ARPG.CAMERA_SCALE_FACTOR;
    }
    
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
    
            ARPGBehavior behavior = new ARPGBehavior(window, getTitle());
            setBehavior(behavior);
            
            createArea();
            return true;
        }
        
        return false;
    }
    
    /**
     * Register the default background and the foreground
     */
    protected void registerBackgroundAndForeground() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
    
    /**
     * Register the doors corresponding to the given parameters, as arrays.
     * Each entry of the arrays corresponds to the metadata of a specific Door.
     * @param areaKeys  (String[]) The keys of the areas to go to
     * @param destinationCoords (DiscreteCoordinates[]) The destination coordinates
     * @param orientations (Orientation[])
     * @param positions (DiscreteCoordinates[]) The position coordinates
     * @param otherCells (DiscreteCoordinates[][]) The other cells, possibly
     */
    protected void registerDoors(String[] areaKeys, DiscreteCoordinates[] destinationCoords, Orientation[] orientations, DiscreteCoordinates[] positions, DiscreteCoordinates[]... otherCells) {
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
