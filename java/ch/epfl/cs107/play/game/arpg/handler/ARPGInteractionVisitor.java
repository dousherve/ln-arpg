package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
    
    /// Add Interaction method with all non Abstract Interactable
    
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
    
    
    /**
     * Simulate an interaction between RPG Interactor and a Grass
     * @param grass (Grass), not null
     */
    default void interactWith(Grass grass){
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Bomb
     * @param bomb (Bomb), not null
     */
    default void interactWith(Bomb bomb){
        // by default the interaction is empty
    }
    
}
