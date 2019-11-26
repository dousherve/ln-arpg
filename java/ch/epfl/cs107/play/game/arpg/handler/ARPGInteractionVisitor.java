package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
    
    /**
     * Interaction with a cell
     * @param cell (ARPGBehavior.ARPGCell) the cell to interact with
     */
    default void interactWith(ARPGBehavior.ARPGCell cell) {
    
    }
    
    /**
     * Interaction with a player
     * @param player (ARPGPlayer) the player to interact with
     */
    default void interactWith(ARPGPlayer player) {
    
    }
    
}
