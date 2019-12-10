package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.item.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
import ch.epfl.cs107.play.game.arpg.actor.monster.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.monster.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.monster.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
    
    /// Add Interaction method with all non Abstract Interactable
    
    /**
     * Interaction with a cell
     * @param cell (ARPGBehavior.ARPGCell) the cell to interact with
     */
    default void interactWith(ARPGBehavior.ARPGCell cell) {
        // by default the interaction is empty
    }
    
    /**
     * Interaction with a player
     * @param player (ARPGPlayer) the player to interact with
     */
    default void interactWith(ARPGPlayer player) {
        // by default the interaction is empty
    }
    
    
    /**
     * Simulate an interaction between RPG Interactor and a Grass
     * @param grass (Grass), not null
     */
    default void interactWith(Grass grass) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Bomb
     * @param bomb (Bomb), not null
     */
    default void interactWith(Bomb bomb) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Coin
     * @param coin (Coin), not null
     */
    default void interactWith(Coin coin) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a Heart
     * @param heart (Heart), not null
     */
    default void interactWith(Heart heart) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a CastleKey
     * @param key (CastleKey), not null
     */
    default void interactWith(CastleKey key) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a CastleDoor
     * @param door (CastleDoor), not null
     */
    default void interactWith(CastleDoor door) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a FlameSkull
     * @param skull (FlameSkull), not null
     */
    default void interactWith(FlameSkull skull) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a LogMonster
     * @param logMonster (LogMonster), not null
     */
    default void interactWith(LogMonster logMonster) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a DarkLord
     * @param lord (DarkLord), not null
     */
    default void interactWith(DarkLord lord) {
        // by default the interaction is empty
    }
    
    // TODO: see if we can solve the problem of the "automatic pick-up" for an ARPGCollectableAreaEntity
    
}
