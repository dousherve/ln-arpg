package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a way of taking into account the fact
 * that an attacker that attacks continously should wait before
 * dealing damage again to a certain Interactable.
 */
public class ContinuousAttackHandler implements Updatable {
    
    private final float ATTACK_DELAY;
    /// A Map associating a certain Interactable with the delay after
    /// which he can take damage again.
    private Map<Interactable, Float> damageDelayMap;
    
    protected ContinuousAttackHandler(float delay) {
        ATTACK_DELAY = delay;
        this.damageDelayMap = new HashMap<>();
    }
    
    /**
     * Check if the attacker can harm the given Interactable
     * @param toHarm (Interactable) The Interactable to harm
     * @return (boolean) a boolean indicating if we can harm it
     */
    protected boolean canHarm(Interactable toHarm) {
        if (damageDelayMap.containsKey(toHarm)) {
            if (damageDelayMap.get(toHarm) <= 0) {
                damageDelayMap.remove(toHarm);
            }
            return false;
        } else {
            damageDelayMap.put(toHarm, ATTACK_DELAY);
            return true;
        }
    }
    
    @Override
    public void update(float deltaTime) {
        // Decrement the attack delays
        for (Interactable interactable : damageDelayMap.keySet()) {
            float delay = damageDelayMap.get(interactable) - deltaTime;
            damageDelayMap.replace(interactable, delay);
        }
    }
    
}
