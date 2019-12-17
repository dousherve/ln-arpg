package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.Inventory;

import java.util.Arrays;

public class ARPGInventory extends Inventory {
    
    /// The maximum weight an ARPGInventory can carry
    private static final float MAX_WEIGHT = 5000.f;
    
    /// The money and the fortune corresponding to the current ARPGInventory
    private int money, fortune;
    
    protected ARPGInventory(int money) {
        super(MAX_WEIGHT);
        
        this.money = money;
        this.fortune = money;
    }
    
    /**
     * Add money to the Inventory
     * @param money (int) The amount of money to add to the Inventory
     */
    protected void addMoney(int money) {
        this.money += money;
        this.fortune += money;
    }
    
    /**
     * Remove money from the Inventory
     * @param money (int) The amount of money to add to the Inventory
     */
    protected void removeMoney(int money) {
        this.money = Math.max(this.money - money, 0);
        this.fortune = Math.max(this.fortune - money, 0);
    }
    
    /**
     * @return (int) the amount of money in the Inventory
     */
    protected int getMoney() {
        return money;
    }
    
    /**
     * @return (int) the amount of the fortune in the Inventory
     */
    protected int getFortune() {
        return fortune;
    }
    
    /**
     * Add a given quantity of a specific ARPGItem to the inventory, if possible.
     * @param item  (ARPGItem) The item to add
     * @param quantity (int) The quantity of the item to add
     * @return (boolean) a boolean indicating if the item has succesfully been added
     */
    protected boolean add(ARPGItem item, int quantity) {
        if (super.add(item, quantity)) {
            fortune += item.getPrice() * quantity;
            return true;
        }
        
        return false;
    }
    
    /**
     * Add the given ARPGItem to the inventory, if possible.
     * @param item  (ARPGItem) The item to add
     * @return (boolean) a boolean indicating if the item has succesfully been added
     */
    protected boolean add(ARPGItem item) {
        return this.add(item, 1);
    }
    
    /**
     * Remove a given quantity of a specific ARPGItem from the inventory, if possible.
     * @param item  (ARPGItem) The item to add
     * @param quantity (int) The quantity of the item to add
     * @return (boolean) a boolean indicating if the item has succesfully been added
     */
    protected boolean remove(ARPGItem item, int quantity) {
        if (super.remove(item, quantity)) {
            fortune -= item.getPrice() * quantity;
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a specific ARPGItem from the inventory, if possible.
     * @param item  (ARPGItem) The item to add
     * @return (boolean) a boolean indicating if the item has succesfully been added
     */
    protected boolean remove(ARPGItem item) {
        return this.remove(item, 1);
    }
    
    /**
     * @return (ARPGItem[]) an array containing the items of this ARPGInventory
     */
    protected ARPGItem[] getItems() {
        // We cast every item as an ARPGItem
        return Arrays.copyOf(super.getItems(), super.getItems().length, ARPGItem[].class);
    }
    
}
