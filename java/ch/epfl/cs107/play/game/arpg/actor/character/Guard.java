package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Guard extends Character {

    private static final float DAMAGES = 1f;

    private static final float ATTACK_TIMEOUT = 1f;
    private float attackTimer;

    class GuardHandler extends CharacterHandler {
        @Override
        public void interactWith(ARPGPlayer player) {
            if (state == State.ATTACKING && attackTimer <= 0){
                player.harm(DAMAGES);
                attackTimer = ATTACK_TIMEOUT;
            } else if (state == State.IDLE){
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

        handler = new GuardHandler();

        dialog.resetDialog("Rien à signaler.");
        attackTimer = 0;

        setupAnimation();
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    @Override
    public void harm(float damage) {
        super.harm(damage);
        state = State.ATTACKING;
    }

    @Override
    protected void setupAnimation() {
        super.setupAnimation();
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/guard", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(10 / 2, sprites);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (state == State.ATTACKING){
            attackTimer = Math.max( attackTimer - deltaTime, 0);
        }
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }
}
