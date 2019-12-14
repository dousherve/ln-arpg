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

    protected enum State{
        IDLE, STOPPED, ATTACKING, VANISHING
    }

    public enum Vulnerability{
        PHYSICAL, MAGIC, FIRE
    }

    protected class CharacterHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            state = State.STOPPED;
        }
    }


    private static final String[] DEFAULT_SENTENCES = new String[]{
            "dialog_1", "dialog_2",
    };

    private static final int ACTION_RADIUS = 2;

    /// State of the character
    protected State state;

    protected static float hp;

    /// InteractionVisitor handler
    protected CharacterHandler handler;

    // MARK:- Animation

    /// Animations array
    protected Animation[] movingAnimations;
    private static final int MOVING_ANIMATION_DURATION = 10;
    /// The vanish Animation
    private Animation vanishAnimation;
    private static final int VANISH_ANIMATION_DURATION = 2;


    private static final float PROBABILITY_TO_CHANGE_ORIENTATION = 0.3f;

    //TEST

    protected Dialog dialog;
    private boolean showDialog = false;

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

        hp  = 5f;

        String text = XMLTexts.getText(DEFAULT_SENTENCES[RandomGenerator.getInstance().nextInt(DEFAULT_SENTENCES.length)]);
        dialog = new Dialog(text, "zelda/dialog", getOwnerArea());

        handler = new CharacterHandler();
        state = State.IDLE;
    }

    /**
     *  Initialize animations
     */
    protected void setupAnimation(){
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/character", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(MOVING_ANIMATION_DURATION / 2, sprites);

        Sprite[] vanishSprites = new Sprite[7];
        for (int i = 0; i < vanishSprites.length; ++i) {
            vanishSprites[i] = new RPGSprite("zelda/vanish", 2f, 2f, this,
                    new RegionOfInterest(32 * i, 0, 32, 32));
        }
        vanishAnimation = new Animation(VANISH_ANIMATION_DURATION, vanishSprites, false);

    }


    /**
     * Randomly orientate the Character
     */
    protected void randomlyOrientate() {
        int randomIndex = RandomGenerator.getInstance().nextInt(4);
        orientate(Orientation.fromInt(randomIndex));
    }

    /**
     * Randomly move the Character
     */
    protected void randomlyMove() {
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
        setShowDialog();
    }

    /**
     *  Show/hide the dialog
     */
    protected void setShowDialog(){
        showDialog = !showDialog;
    }

    public void harm(Vulnerability vulnerability, float damage){
        hp = Math.max(hp - damage, 0);
    }

    protected void die(){
        state = State.VANISHING;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (hp <= 0){
            die();
        }

        if (state == State.IDLE) {
            randomlyMove();
            movingAnimations[getOrientation().ordinal()].update(deltaTime);

        }else if (state == State.VANISHING){
            vanishAnimation.update(deltaTime);
            System.out.println("vanish");
            if (vanishAnimation.isCompleted()){
                getOwnerArea().unregisterActor(this);
            }

        } else {
            movingAnimations[getOrientation().ordinal()].reset();
            state = State.IDLE;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (state != State.VANISHING){
            movingAnimations[getOrientation().ordinal()].draw(canvas);
        } else {
            vanishAnimation.draw(canvas);
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
        final DiscreteCoordinates CURRENT_COORDS = getCurrentMainCellCoordinates();

        final int X_START_COORD = CURRENT_COORDS.x - ACTION_RADIUS;
        final int X_END_COORD = CURRENT_COORDS.x + ACTION_RADIUS;
        final int Y_START_COORD = CURRENT_COORDS.y - ACTION_RADIUS;
        final int Y_END_COORD = CURRENT_COORDS.y + ACTION_RADIUS;

        List<DiscreteCoordinates> coords = new ArrayList<>();

        for (int i = X_START_COORD; i <= X_END_COORD; ++i) {
            for (int j = Y_START_COORD; j <= Y_END_COORD; ++j) {
                if (
                        i >= 0 && i < getOwnerArea().getWidth() &&
                                j >= 0 && j < getOwnerArea().getHeight() &&
                                !(i == CURRENT_COORDS.x && j == CURRENT_COORDS.y)
                ) {
                    coords.add(new DiscreteCoordinates(i, j));
                }
            }
        }

        return coords;
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
