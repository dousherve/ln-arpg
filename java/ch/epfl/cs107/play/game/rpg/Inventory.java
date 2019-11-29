package ch.epfl.cs107.play.game.rpg;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class Inventory {
    
    public interface Holder {
        
        /**
         * @param item (Item) An InventoryItem
         * @return (boolean) A boolean indicating whether or not the Holder possess the item
         */
        boolean possess(InventoryItem item);
        
    }
    
    /// The maximum weight that this Inventory can carry
    private float maxWeight;
    /// The Map matching all the items of the Inventory with their quantities
    private HashMap<InventoryItem, Integer> items;
    
    protected Inventory(float maxWeight) {
        this.maxWeight = maxWeight;
        items = new HashMap<>();
    }
    
    /**
     * Compute the total weight of the Inventory.
     * @return  (float) The total weight
     */
    private float totalWeight() {
        float totalWeight = 0;
        for (Map.Entry<InventoryItem, Integer> entry : items.entrySet()) {
            totalWeight += entry.getKey().getWeight() * entry.getValue();
        }
        return totalWeight;
    }
    
    /**
     * Add a certain quantity of a given InventoryItem to the Inventory.
     * @param item  (InventoryItem) The item to add
     * @param quantity (int) The number of items to be added
     * @return (boolean) A boolean indicating whether or not the elements have been added
     */
    protected boolean add(InventoryItem item, int quantity) {
        if (quantity <= 0) {
            return false;
        }
    
        int currentQuantity = items.getOrDefault(item, 0);
        float weight = item.getWeight() * quantity;
        
        if (totalWeight() + weight < maxWeight) {
            if (currentQuantity == 0) {
                items.put(item, quantity);
            } else {
                items.replace(item, currentQuantity + quantity);
            }
    
            System.out.println("Added " + quantity + " " + item.getName());
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a certain quantity of a given InventoryItem from the Inventory.
     * @param item  (InventoryItem) The item to remove
     * @param quantity (int) The number of items to be removed
     * @return (boolean) A boolean indicating whether or not the elements have been removed
     */
    protected boolean remove(InventoryItem item, int quantity) {
        int currentQuantity = items.getOrDefault(item, 0);
        
        if (quantity <= 0 || currentQuantity == 0 || quantity > currentQuantity) {
            return false;
        }
        
        if (quantity == currentQuantity) {
            items.remove(item);
        } else {
            // We have handled all the other cases, here we finally remove the given quantity
            items.replace(item, currentQuantity - quantity);
        }
        
        return true;
    }
    
    /**
     * @param item (InventoryItem) An InventoryItem
     * @return (boolean) A boolean indicating whether or not the Inventory contains a given item
     */
    public boolean contains(InventoryItem item) {
        return items.containsKey(item);
    }
    
    /**
     * @param item (InventoryItem) An InventoryItem
     * @return (int) The quantity of the given InventoryItem ; throws NoSuchElementException if
     *         it does not exist in the items Map
     */
    public int getQuantity(InventoryItem item) {
        if (contains(item)) {
            return items.get(item);
        }
        
        throw new NoSuchElementException("The inventory does not contain the item " + item.getName());
    }
    
    /**
     * @return A boolean indicating wheteher or not the Inventory
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * @return (Set) A set of the items of the inventory
     */
    public Set<InventoryItem> getItems() {
        return items.keySet();
    }
    
}
