package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;

public class Grotte2 extends ARPGArea {


    @Override
    protected void createArea() {
        // Background
        registerActor(new Background(this));

    }

    @Override
    public String getTitle() {
        return "GrotteMew";
    }
}
