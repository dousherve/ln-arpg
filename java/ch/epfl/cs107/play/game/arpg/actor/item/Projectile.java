package ch.epfl.cs107.play.game.arpg.actor.item;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Projectile extends MovableAreaEntity implements Interactor {

    /// speed in case/second
    private int speed;
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
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, float maximumDistance) {
        super(area, orientation, position);
        this.speed = speed;
        this.maximumDistance = maximumDistance;
    }

    public void stop(){
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void update(float deltaTime) {
        distance += deltaTime*speed;
        if (distance >= maximumDistance){
            stop();
            return;
        }
        move(0);
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

}
