package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.*;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
    
    public static final float CAMERA_SCALE_FACTOR = 13.f;
    
    private void createAreas() {
        addArea(new Ferme());
        addArea(new Village());
        addArea(new Route());
        addArea(new RouteChateau());
        addArea(new Chateau());
        addArea(new RouteTemple());
        addArea(new Temple());
        addArea(new Grotte());
        addArea(new Grotte2());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(getWindow().getKeyboard().get(Keyboard.ESC).isPressed()) {
            getCurrentArea().setPaused();
        }
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            
            setCurrentArea("zelda/Ferme", true);
            
            initPlayer(new ARPGPlayer(getCurrentArea(), Orientation.DOWN, new DiscreteCoordinates(6,10)));
            return true;
        }
        
        return false;
    }

    @Override
    public String getTitle() {
        return "ZeldLN";
    }
    
}
