package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;

public enum ARPGItem implements InventoryItem {
    
    ARROW("arrow.icon"), SWORD("sword.icon"), BOW("bow.icon"),
    BOMB("bomb"), STAFF("staff_water.icon"), CASTLE_KEY("key"),
    HEAL_POTION("potion.heal");
    
    private Sprite sprite;
    private String resourceName;
    
    ARPGItem(String resourceName) {
        this.resourceName = resourceName;

        sprite = new RPGSprite("zelda/" + resourceName, 0.75f, 0.75f, null,
                new RegionOfInterest(0, 0, 16, 16));
    }
    
    @Override
    public String getName() {
        switch (this) {
            case ARROW:
            case SWORD:
            case BOW:
            case BOMB:
            case STAFF:
            case HEAL_POTION:
                // For all of those names, we just return the name in lowercase, capitalized
                String lower = toString().toLowerCase();
                return lower.substring(0, 1).toUpperCase().concat(lower.substring(1));
            case CASTLE_KEY:
                return "CastleKey";
        }
        
        return "ERROR";
    }

    public String getResourceName() {
        return resourceName;
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    
    @Override
    public float getWeight() {
        switch (this) {
            case ARROW:
                return 2;
            case SWORD:
                return 10;
            case BOW:
                return 5;
            case BOMB:
                return 3;
            case STAFF:
                return 7;
            case CASTLE_KEY:
            case HEAL_POTION:
                return 1;
            default:
                return 0;
        }
    }
    
    @Override
    public int getPrice() {
        switch (this) {
            case ARROW:
                return 1;
            case SWORD:
                return 10;
            case BOW:
                return 7;
            case BOMB:
                return 15;
            case STAFF:
                return 30;
            case HEAL_POTION:
                return 25;
            case CASTLE_KEY:
                return 80;
        }
        
        // ERROR
        return -1;
    }
    
}
