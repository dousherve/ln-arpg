package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;

public enum ARPGItem implements InventoryItem {
    
    ARROW("arrow.icon"), SWORD("sword.icon"), BOW("bow.icon"),
    BOMB("bomb"), STAFF("staff_water.icon"), CASTLE_KEY("key");
    
    Sprite sprite;
    
    ARPGItem(String resourceName) {
        sprite = new RPGSprite("zelda/" + resourceName, 0.5f, 0.5f, null,
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
                // For all of those names, we just return the name in lowercase, capitalized
                String lower = toString().toLowerCase();
                return lower.substring(0, 1).toUpperCase().concat(lower.substring(1));
            case CASTLE_KEY:
                return "CastleKey";
        }
        
        return "TO_IMPLEMENT";
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    
    @Override
    public float getWeight() {
        return 0;
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
            case CASTLE_KEY:
                return 80;
        }
        
        return -1;
    }
    
}
