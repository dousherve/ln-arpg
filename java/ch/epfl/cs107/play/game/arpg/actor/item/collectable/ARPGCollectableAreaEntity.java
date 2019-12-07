package ch.epfl.cs107.play.game.arpg.actor.item.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

abstract public class ARPGCollectableAreaEntity extends CollectableAreaEntity {
    
    /**
     * Default ARPGCollectableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public ARPGCollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }
    
    @Override
    public boolean isCellInteractable() {
        return true;
    }
    
    @Override
    public boolean isViewInteractable() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        getOwnerArea().unregisterActor(this);
    }
    
}
