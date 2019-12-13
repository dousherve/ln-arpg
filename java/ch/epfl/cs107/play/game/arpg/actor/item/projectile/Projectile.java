package ch.epfl.cs107.play.game.arpg.actor.item.projectile;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements Interactor, FlyableEntity {
    
    /// Frame rate
    private static final int FRAME_RATE = 24;
    
    /// Speed in cell/second
    private float speed;
    
    /// Maximum travelling distance of the projectile
    private float maximumDistance;
    /// Traveled distance
    private float distance;
    
    private List<Interactable> interactedEntities;

    /**
     * Default Projectile constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param speed       (float) speed
     * @param maximumDistance (float) maximum distance
     */
    protected Projectile(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position);
        
        this.speed = speed;
        this.maximumDistance = maximumDistance;
        
        interactedEntities = new ArrayList<>();
    
        distance = 0;
    }
    
    /**
     * Stop the projectile by unregistering it
     */
    public void stop() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        distance += deltaTime * speed;
        if (distance >= maximumDistance){
            stop();
            return;
        }
        
        // Stop if there is a collision
        if (!isDisplacementOccurs() && !move(((int) (FRAME_RATE / speed)))) {
            stop();
        }
    }
    
    /**
     * Indicate if we can interact with the given Interactable,
     * in other words indicate if the Projectile already has interacted with it
     * @param interactable (Interactable) not null
     * @return (boolean) a boolean indicating if we can interact with the given Interactable
     */
    final protected boolean canInteractWith(Interactable interactable) {
        if (interactedEntities.contains(interactable)){
            return false;
        } else {
            interactedEntities.add(interactable);
            return true;
        }
    }

    // MARK:- Interactable
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
    // MARK:- Interactor
    
    @Override
    public boolean wantsCellInteraction() {
        return distance <= maximumDistance;
    }
    
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }
    
}
