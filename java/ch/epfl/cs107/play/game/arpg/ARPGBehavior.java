package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {
    
    public enum ARPGCellType {
        
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, false),
        DOOR(-195580, true, false),
        WALKABLE(-1, true, false);
        
        final int type;
        final boolean isWalkable, isFlyable;
    
        ARPGCellType(int type, boolean isWalkable, boolean isFlyable) {
            this.type = type;
            this.isWalkable = isWalkable;
            this.isFlyable = isFlyable;
        }
        
        public static ARPGCellType toType(int type) {
            for (ARPGCellType ct : ARPGCellType.values()) {
                if (ct.type == type)
                    return ct;
            }
            
            return NULL;
        }
        
    }
    
    public class ARPGCell extends AreaBehavior.Cell {
        
        private final ARPGCellType type;
        
        /**
         * Default ARPGCell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        protected ARPGCell(int x, int y, ARPGCellType type) {
            super(x, y);
            this.type = type;
        }
        
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }
        
        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity instanceof FlyableEntity) {
                return type.isWalkable || ((FlyableEntity) entity).canFly() && type.isFlyable;
            }
            
            return type.isWalkable && !hasNonTraversableContent();
        }
        
        @Override
        public boolean isCellInteractable() {
            return true;
        }
        
        @Override
        public boolean isViewInteractable() {
            return true;
        }
        
        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ARPGInteractionVisitor) v).interactWith(this);
        }
        
    }
    
    /**
     * Default ARPGBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ARPGBehavior(Window window, String name) {
        super(window, name);
        
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ARPGCellType color = ARPGCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ARPGCell(x, y, color));
            }
        }
    }
    
    @Override
    protected void cellInteractionOf(Interactor interactor) {
        super.cellInteractionOf(interactor);
    }
    
    @Override
    protected void viewInteractionOf(Interactor interactor) {
        super.viewInteractionOf(interactor);
    }
    
}
