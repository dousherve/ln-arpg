package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.terrain.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.terrain.Grass;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.item.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.item.projectile.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.item.projectile.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.item.projectile.Projectile;
import ch.epfl.cs107.play.game.arpg.actor.monster.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.monster.FireSpell;
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
     * Simulate an interaction between RPG Interactor and a CastleDoor
     * @param castleDoor (CastleDoor), not null
     */
    default void interactWith(CastleDoor castleDoor) {
        // by default the interaction is empty
    }
    
    // MARK:- ARPGCollecatbleAreaEntity
    
    /**
     * Simulate an interaction between RPG Interactor and a ARPGCollectableAreaEntity
     * @param entity (ARPGCollectableAreaEntity), not null
     */
    default void interactWith(ARPGCollectableAreaEntity entity) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Coin
     * @param coin (Coin), not null
     */
    default void interactWith(Coin coin) {
        interactWith((ARPGCollectableAreaEntity) coin);
    }

    /**
     * Simulate an interaction between RPG Interactor and a Heart
     * @param heart (Heart), not null
     */
    default void interactWith(Heart heart) {
        interactWith((ARPGCollectableAreaEntity) heart);
    }

    /**
     * Simulate an interaction between RPG Interactor and a CastleKey
     * @param key (CastleKey), not null
     */
    default void interactWith(CastleKey key) {
        interactWith((ARPGCollectableAreaEntity) key);
    }
    
    // MARK:- Monster
    
    /**
     * Simulate an interaction between RPG Interactor and a Monster
     * @param monster (Monster), not null
     */
    default void interactWith(Monster monster) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a FireSpell
     * @param fireSpell (FireSpell), not null
     */
    default void interactWith(FireSpell fireSpell) {
        // by default the interaction is empty
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a FlameSkull
     * @param skull (FlameSkull), not null
     */
    default void interactWith(FlameSkull skull) {
        interactWith((Monster) skull);
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a LogMonster
     * @param logMonster (LogMonster), not null
     */
    default void interactWith(LogMonster logMonster) {
        interactWith((Monster) logMonster);
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a DarkLord
     * @param darkLord (DarkLord), not null
     */
    default void interactWith(DarkLord darkLord) {
        interactWith((Monster) darkLord);
    }
    
    // MARK:- Projectile
    
    /**
     * Simulate an interaction between RPG Interactor and a Projectile
     * @param projectile (Projectile), not null
     */
    default void interactWith(Projectile projectile) {
        projectile.stop();
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Arrow
     * @param arrow (Arrow), not null
     */
    default void interactWith(Arrow arrow) {
        interactWith((Projectile) arrow);
    }
    
    /**
     * Simulate an interaction between RPG Interactor and a Arrow
     * @param arrow (Arrow), not null
     */
    default void interactWith(MagicWaterProjectile arrow) {
        interactWith((Projectile) arrow);
    }
    
}
