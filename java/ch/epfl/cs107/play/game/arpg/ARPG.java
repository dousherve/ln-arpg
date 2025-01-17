package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.*;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {

    public static final float CAMERA_SCALE_FACTOR = 13.f;

    // Pause - Death images
    private ImageGraphics deathImg;
    private static final float DEATH_IMG_WIDTH = 8f, DEATH_IMG_HEIGHT = 3f;

    private ImageGraphics pauseImg;

    //Alpha of death message
    private static final float ALPHA_MAX = 1;
    private float alpha;

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

        alpha = Math.max(alpha-deltaTime/5, 0);

        if (((ARPGPlayer) getPlayer()).isDead()) {
            getPlayer().leaveArea();

            alpha = ALPHA_MAX;

            setCurrentArea("zelda/Ferme", true);
            getPlayer().enterArea(getCurrentArea(), new DiscreteCoordinates(6,10));
            ((ARPGPlayer) getPlayer()).heal(5f);
        }

        // Draw pause graphic
        if (getCurrentArea().isPaused()) {
            Vector bottomLeftAnchor = getWindow().getTransform().getOrigin().sub(new Vector(getWindow().getScaledWidth() / 2, getWindow().getScaledHeight() / 2));

            pauseImg.setAnchor(bottomLeftAnchor);
            pauseImg.draw(getWindow());
        }

        // Draw pause message
        if (alpha > 0) {
            deathImg.setAnchor(getWindow().getTransform().getOrigin().sub(new Vector(DEATH_IMG_WIDTH /2, 0f)));
            deathImg.setAlpha(alpha);
            deathImg.draw(getWindow());
        }

        if(getWindow().getKeyboard().get(Keyboard.ESC).isPressed()) {
            getCurrentArea().setPaused();
        }
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();

            setCurrentArea("zelda/Ferme", true);

            pauseImg = new ImageGraphics(ResourcePath.getSprite("zelda/pause"), getCurrentArea().getCameraScaleFactor(), getCurrentArea().getCameraScaleFactor(), new RegionOfInterest(0,0, 320,320));
            deathImg = new ImageGraphics(ResourcePath.getSprite("zelda/death_message"), DEATH_IMG_WIDTH, DEATH_IMG_HEIGHT, new RegionOfInterest(0,0, 96,32));

            pauseImg.setDepth(Float.MAX_VALUE);
            deathImg.setDepth(Float.MAX_VALUE);

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
