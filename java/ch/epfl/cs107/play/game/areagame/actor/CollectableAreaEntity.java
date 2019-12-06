package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

abstract public class CollectableAreaEntity extends AreaEntity {
    
    /// Keeps track whether the entity is collected or not
    private boolean isCollected = false;
    
    /**
     * Default CollectableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }
    
    /**
     * Mark the current entity as collected and remove it from the current Area.
     */
    public void setCollected() {
        if (!isCollected) {
            isCollected = true;
            // TODO: remove debug sout
            System.out.println("Collected");
            getOwnerArea().unregisterActor(this);
        }
    }
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }
    
}
