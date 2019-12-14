package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.monster.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Guard extends Character {

    private static final float DAMAGES = 1f;

    class GuardHandler extends CharacterHandler {

        @Override
        public void interactWith(ARPGPlayer player) {
            if (state == State.ATTACKING){
                player.harm(DAMAGES);
            } else {
                state = State.STOPPED;
            }
        }
    }

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Guard(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        dialog.resetDialog("Rien à signaler.");
        handler = new GuardHandler();
        hp = 5f;
        setupAnimation();
    }


    @Override
    protected void setupAnimation() {
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/guard", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(10 / 2, sprites);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public boolean wantsViewInteraction() {
        return (state==State.ATTACKING);
    }
}
