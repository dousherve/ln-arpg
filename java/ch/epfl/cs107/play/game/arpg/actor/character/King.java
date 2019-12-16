package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class King extends Character {

    private Sprite sprite;

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public King(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        dialog.resetDialog("Merci de m'avoir délivré!");
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(Canvas canvas) {
        if (showDialog){
            dialog.draw(canvas);
        }
        sprite.draw(canvas);
    }

    @Override
    protected void setupAnimation() {
        sprite = new Sprite("zelda/king", 1f, 2f, this, new RegionOfInterest(0, 32*2, 16, 32));
    }
}
