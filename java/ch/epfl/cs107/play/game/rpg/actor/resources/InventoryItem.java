package ch.epfl.cs107.play.game.rpg.actor.resources;

public interface InventoryItem {
    
    /**
     * @return (String) The name of the item
     */
    String getName();
    
    /**
     * @return (float) The weight of the item
     */
    float getWeight();
    
    /**
     * @return (int) The price of the item
     */
    int getPrice();
    
}
