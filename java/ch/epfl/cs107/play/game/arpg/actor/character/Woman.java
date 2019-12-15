package ch.epfl.cs107.play.game.arpg.actor.character;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Woman extends Character {

    private boolean walkToNextMap = false;
    protected static final int MOVING_ANIMATION_DURATION = 10;

    /**
     * Default Character constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Woman(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        state = State.STOPPED;
        if (getOwnerArea().getTitle().equals("zelda/Route")){
            dialog.resetDialog("Je cherche un héro avec une épée");
        } else {
            dialog.resetDialog(XMLTexts.getText("templeDoor"));
        }
    }

    public void beginQuest() {
        if (getOwnerArea().getTitle().equals("zelda/Route")) {
            dialog.resetDialog("Tu es le garçon avec l'épée? Suis-moi");
            super.personalInteraction();

            if (!showDialog && getOwnerArea().getTitle().equals("zelda/Route")) {
                walkToNextMap = true;
                orientate(Orientation.RIGHT);
            }
        } else {
            super.personalInteraction();
        }
    }

    @Override
    protected void setupAnimation() {
        super.setupAnimation();
        Sprite[][] sprites = RPGSprite.extractSprites("zelda/woman", 4,
                1, 2, this, 16, 32,
                new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
        movingAnimations = RPGSprite.createAnimations(10 / 2, sprites);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        state = State.SPECIAL;

        if (walkToNextMap) {
            if (!move(MOVING_ANIMATION_DURATION) && !isDisplacementOccurs()){
                getOwnerArea().unregisterActor(this);
            }
            movingAnimations[getOrientation().ordinal()].update(deltaTime);
        }
    }

    @Override
    public boolean isViewInteractable() {
        return !walkToNextMap;
    }

    @Override
    public boolean wantsViewInteraction() {
        return !walkToNextMap;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        if  (!walkToNextMap){
            ((ARPGInteractionVisitor) v).interactWith(this);
        }
    }
}
