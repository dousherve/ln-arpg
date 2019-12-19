package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Guard extends Character {
    
    private class GuardHandler extends CharacterHandler {
        
        @Override
        public void interactWith(ARPGPlayer player) {
            if (state == State.ATTACKING && attackTimer <= 0 && attackAnimation.isCompleted()) {
                player.harm(DAMAGE);
                attackAnimation.reset();
                attackTimer = ATTACK_TIMEOUT;
            } else if (state == State.IDLE) {
                state = State.STOPPED;
            }
            
            if (player.isDead()) {
                state = State.IDLE;
            }
        }
        
    }

    private static final float DAMAGE = 1f;

    private static final float ATTACK_TIMEOUT = 1f;
    private float attackTimer;

    private Animation attackAnimation;
    private static final int ACTION_ANIMATION_DURATION = 1;

    /**
     * Default Guard constructor
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

        Sprite[] spritesAttack = new Sprite[4];
        for (int i = 0; i < spritesAttack.length; ++i) {
            spritesAttack[i] = new RPGSprite(
                    "zelda/guard.sword", 2f, 2f, this, 
                    new RegionOfInterest(32 * i, 0, 32, 32)
            );
        }
        attackAnimation = new Animation(ACTION_ANIMATION_DURATION, spritesAttack, false);
    }

    @Override
    public void draw(Canvas canvas) {
        if (state == State.ATTACKING && !attackAnimation.isCompleted()) {
            attackAnimation.draw(canvas);
        } else {
            super.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (state == State.ATTACKING) {
            attackTimer = Math.max(attackTimer - deltaTime, 0);
            attackAnimation.update(deltaTime);
        }
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }
    
}
