package ch.epfl.cs107.play.game.areagame.actor;

public interface FlyableEntity {
    
    /**
     *
     * @return (boolean) Whether the current entity can fly or not
     */
    default boolean canFly() {
        return true;
    }
    
}
