package ch.epfl.cs107.play.game.arpg.actor.attacker;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.arpg.actor.item.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.item.Grass;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends Monster implements FlyableEntity {
    
    private class FlameSkullHandler implements ARPGInteractionVisitor {
    
        @Override
        public void interactWith(ARPGPlayer player) {
            if (attackHandler.canHarm(player)) {
                player.harm(DAMAGE);
            }
        }
    
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }
    
        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }
        
        @Override
        public void interactWith(Monster monster) {
            if (attackHandler.canHarm(monster)) {
                monster.harm(Vulnerability.FIRE, DAMAGE);
            }
        }
        
    }
    
    /// The maximum Health Points
    private static final float MAX_HP = 2f;
    /// The minimum and maximum lifetimes, respectively
    private static final float MIN_LIFE_TIME = 2f, MAX_LIFE_TIME = 20f;
    
    /// The actual lifetime of the FlameSkull
    private float lifetime;
    
    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;
    
    /// The damage the FlameSkull deals
    private static final float DAMAGE = 1.5f;
    
    /// The size of the FlameSkull
    private static final float SIZE = 2f;
    
    private static final float ATTACK_DELAY = 0.3f;
    
    /// The Interaction handler
    private FlameSkullHandler handler;
    
    /// The continuous attack handler
    private ContinuousAttackHandler attackHandler;
    
    /// Animations array
    private Animation[] animations;
    /// Index of the current animation in the above-mentioned array
    private int animationIndex;
    /// Animation duration in number of frames
    private static final int ANIMATION_DURATION = 16;
    
    /**
     * Default FlameSkull constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): orientation of the entity
     * @param position        (Coordinate): Initial position of the entity. Not null
     */
    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, MAX_HP, Vulnerability.PHYSICAL, Vulnerability.MAGIC);
        
        handler = new FlameSkullHandler();
        attackHandler = new ContinuousAttackHandler(ATTACK_DELAY);
        
        // We choose a random value between MIN_LIFE_TIME and MAX_LIFE_TIME
        lifetime = MIN_LIFE_TIME +
                (MAX_LIFE_TIME - MIN_LIFE_TIME) * RandomGenerator.getInstance().nextFloat();
        
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/flameSkull", 3,
                SIZE, SIZE, this, 32, 32,
                new Vector(-SIZE/4, -SIZE/4),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animations = RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
        animationIndex = getOrientation().ordinal();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (isAlive()) {
            lifetime -= deltaTime;
            if (lifetime <= 0) {
                die();
                return;
            }
            
            attackHandler.update(deltaTime);
    
            randomlyMove(ANIMATION_DURATION);
    
            // Compute the index of the animation to draw
            animationIndex = getOrientation().ordinal();
            if (isDisplacementOccurs()) {
                animations[animationIndex].update(deltaTime);
            } else {
                animations[animationIndex].reset();
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (isAlive()) {
            animations[animationIndex].draw(canvas);
        }
    }
    
    // MARK:- Interactable
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
    
    // MARK:- Interactor
    
    @Override
    public boolean wantsCellInteraction() {
        // We check if the monster has changed cell, in order not to deal the damage mutliple times
        return isAlive();
    }
    
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
    
}
