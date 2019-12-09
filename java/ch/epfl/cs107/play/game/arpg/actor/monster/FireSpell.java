package ch.epfl.cs107.play.game.arpg.actor.monster;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collections;
import java.util.List;

public class FireSpell extends AreaEntity implements Interactable {

    ///time in simulation frame
    private int lifetime;

    private static final int MIN_LIFE_TIME = 120;
    private static final int MAX_LIFE_TIME = 240;

    private static final int PROPAGATION_TIME_FIRE = 150;

    private static final float SIZE = 1f;

    private static final float DAMAGE = 0.5f;

    private Orientation orientation;

    private int strength;

    // MARK:- Animation:
    private Animation animation;
    private static final int ANIMATION_DURATION = 2;




    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int strength) {
        super(area, orientation, position);

        Sprite[] sprites = new Sprite[7];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new RPGSprite("zelda/fire", SIZE, SIZE, this,
                    new RegionOfInterest(16*i, 0, 16, 16),
                    new Vector(0,0));
        }
        animation = new Animation(ANIMATION_DURATION, sprites, true);

        this.strength = Math.max(strength, 0);

    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
