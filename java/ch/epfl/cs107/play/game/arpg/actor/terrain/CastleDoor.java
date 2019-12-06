package ch.epfl.cs107.play.game.arpg.actor.terrain;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {

    private ImageGraphics image;

    private String openImage = ResourcePath.getSprite("zelda/castledDoor.open");
    private String closeImage = ResourcePath.getSprite("zelda/castleDoor.open");


    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, otherSideCoordinates, signal, area, orientation, position);

        image = new ImageGraphics(openImage, 1.5f,1.5f,
                new RegionOfInterest(0,0,32,32));

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        image.draw(canvas);

    }
}
