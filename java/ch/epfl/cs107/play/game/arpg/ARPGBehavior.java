package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {
    
    public class ARPGCell extends AreaBehavior.Cell {
        
        /**
         * Default ARPGCell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        protected ARPGCell(int x, int y) {
            super(x, y);
        }
        
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }
        
        @Override
        protected boolean canEnter(Interactable entity) {
            return true;
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
                setCell(x, y, new ARPGCell(x, y));
            }
        }
    }
    
}
