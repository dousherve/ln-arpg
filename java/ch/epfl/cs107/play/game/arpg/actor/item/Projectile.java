package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Projectile extends MovableAreaEntity implements Interactor, FlyableEntity {

    /// speed in case/second
    private float speed;
    /// maximum distance for the projectile (in case)
    private float maximumDistance;
    /// traveled distance
    private float distance;


    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param speed       (float) speed in cell/Second
     * @param maximumDistance (float) maximum distance in cell
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, float speed, float maximumDistance) {
        super(area, orientation, position);
        this.speed = speed;
        this.maximumDistance = maximumDistance;
    }

    public void stop(){
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        distance += deltaTime*speed;
        if (distance >= maximumDistance){
            stop();
            return;
        }
        move(5);
    }

    // MARK:- Interactor

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
