package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character extends MovableAreaEntity implements Interactor {

    protected class CharacterHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            if (state == State.IDLE) {
                state = State.STOPPED;
            }
        }
    }

    protected enum State {
        IDLE, STOPPED, ATTACKING, VANISHING, SPECIAL
    }

    /// Default sentences that a default character can say
    private static final String[] DEFAULT_SENTENCES_KEYS = new String[] {
            "dialog_1", "dialog_2", "dialog_3", "dialog_4", "dialog_5"
    };

    /// The radius of his FOV
    private static final int ACTION_RADIUS = 2;

    /// The State of the character
    protected State state;

    /// The Health Points
    private float hp;

    /// InteractionVisitor handler
    protected CharacterHandler handler;

    // MARK:- Animation

    /// Animations array
    protected Animation[] movingAnimations;
    protected static final int MOVING_ANIMATION_DURATION = 10;

    /// The vanish Animation
    private Animation vanishAnimation;
    private static final int VANISH_ANIMATION_DURATION = 2;

    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;

    /// Default dialog
    protected Dialog dialog;
    protected boolean showDialog = false;

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Character(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        setupAnimation();

        hp = 5f;

        String text = XMLTexts.getText(
                DEFAULT_SENTENCES_KEYS[RandomGenerator.getInstance().nextInt(DEFAULT_SENTENCES_KEYS.length)]
        );

        dialog = new Dialog(text, "zelda/dialog", getOwnerArea());

        handler = new CharacterHandler();
        state = State.IDLE;
    }

    /**
     *  Initialize animation
     */
    protected void setupAnimation(){
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/character", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION / 2, sprites);

        Sprite[] vanishSprites = new Sprite[7];
        for (int i = 0; i < vanishSprites.length; ++i) {
            vanishSprites[i] = new RPGSprite("zelda/vanish", 2f, 2f, this,
                    new RegionOfInterest(32 * i, 0, 32, 32),
                    new Vector(-0.5f,0f));
        }
        vanishAnimation = new Animation(VANISH_ANIMATION_DURATION, vanishSprites, false);
    }


    /**
     * Randomly orientate the Character
     */
    private void randomlyOrientate() {
        int randomIndex = RandomGenerator.getInstance().nextInt(4);
        orientate(Orientation.fromInt(randomIndex));
    }

    /**
     * Randomly move the Character
     */
    private void randomlyMove() {
        if (!isDisplacementOccurs()) {
            if (RandomGenerator.getInstance().nextFloat() < PROBABILITY_TO_CHANGE_ORIENTATION) {
                randomlyOrientate();
            }

            move(MOVING_ANIMATION_DURATION);
        }
    }

    /**
     *  What the character do when the player interact with
     */
    public void personalInteraction(){
        toggleShowDialog();
    }

    /**
     *  Show/hide the dialog
     */
    private void toggleShowDialog(){
        showDialog = !showDialog;
    }

    /**
     * Harm the current Character
     * @param damage (float) The damage to deal to the Character
     */
    public void harm(float damage) {
        hp = Math.max(hp - damage, 0);
    }

    private void die() {
        state = State.VANISHING;
    }

    /**
     * Tell if the character is invincible or not
     * @return (boolean) true if invincible
     */
    public boolean isInvincible(){
        return true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (hp <= 0) {
            die();
        }

        switch (state) {
            case IDLE:
                randomlyMove();
                movingAnimations[getOrientation().ordinal()].update(deltaTime);
                break;

            case VANISHING:
                vanishAnimation.update(deltaTime);
                if (vanishAnimation.isCompleted()) {
                    getOwnerArea().unregisterActor(this);
                }
                break;

            case STOPPED:
                state = State.IDLE;
                movingAnimations[getOrientation().ordinal()].reset();
                break;

            default:
                break;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (hp <= 0){
            die();
        }

        if (state == State.VANISHING) {
            vanishAnimation.draw(canvas);
        } else {
            movingAnimations[getOrientation().ordinal()].draw(canvas);
        }
        if (showDialog){
            dialog.draw(canvas);
        }
    }

    // MARK:- Interactable

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    // MARK:- Interactor

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Interactor.getAllCellsInRadius(
                ACTION_RADIUS, getCurrentMainCellCoordinates(),
                getOwnerArea().getWidth(), getOwnerArea().getHeight()
        );
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }
}
