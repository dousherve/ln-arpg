package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;

public class ARPGInventory extends Inventory {
    
    /// The maximum weight an ARPGInventory can carry
    private static final float MAX_WEIGHT = 5000.f;
    
    /// The money and the fortune corresponding to the current ARPGInventory
    private int money, fortune;
    
    protected ARPGInventory(int money) {
        super(MAX_WEIGHT);
        
        this.money = money;
        fortune = money;
    }
    
    /**
     * Add money to the Inventory
     * @param money (int) The amount of money to add to the Inventory
     */
    protected void addMoney(int money) {
        this.money += money;
    }
    
    /**
     * @return (int) the amount of money in the Inventory
     */
    public int getMoney() {
        return money;
    }
    
    /**
     * @return (int) the amount of the fortune in the Inventory
     */
    public int getFortune() {
        return fortune;
    }
    
    @Override
    protected boolean add(InventoryItem item, int quantity) {
        if (super.add(item, quantity)) {
            fortune += item.getPrice() * quantity;
            return true;
        }
        
        return false;
    }
    
    @Override
    protected boolean remove(InventoryItem item, int quantity) {
        if (super.remove(item, quantity)) {
            fortune -= item.getPrice() * quantity;
            return true;
        }
        
        return false;
    }
    
    @Override
    protected InventoryItem[] getItems() {
        return super.getItems();
    }
}
