package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents Interactor object (i.e. it can interact with some Interactable)
 * @see Interactable
 * This interface makes sense only in the "Area Context" with Actor contained into Area Cell
 */
public interface Interactor {

    /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    List<DiscreteCoordinates> getCurrentCells();


    /**
     * Get this Interactor's current field of view cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    List<DiscreteCoordinates> getFieldOfViewCells();


    /**@return (boolean): true if this require cell interaction */
    boolean wantsCellInteraction();
    /**@return (boolean): true if this require view interaction */
    boolean wantsViewInteraction();

    /**
     * Do this Interactor interact with the given Interactable
     * The interaction is implemented on the interactor side !
     * @param other (Interactable). Not null
     */
    void interactWith(Interactable other);
    
    /**
     * Return a list of all the Cells in a certain radius, excluding the the main Cell.
     * @param radius (int) The radius
     * @param mainCellCoordinates (DiscreteCoordinates) The coordinates of the center of the zone
     * @param areaWidth (int) The width of the Area
     * @param areaHeight (int) The height of the Area
     * @return (List<DiscreteCoordinates>) A list containing all the requested cell coordinates.
     */
    static List<DiscreteCoordinates> getAllCellsInRadius(int radius, DiscreteCoordinates mainCellCoordinates, int areaWidth, int areaHeight) {
        final int X_START_COORD = mainCellCoordinates.x - radius;
        final int X_END_COORD = mainCellCoordinates.x + radius;
        final int Y_START_COORD = mainCellCoordinates.y - radius;
        final int Y_END_COORD = mainCellCoordinates.y + radius;
    
        List<DiscreteCoordinates> coords = new ArrayList<>();
    
        for (int i = X_START_COORD; i <= X_END_COORD; ++i) {
            for (int j = Y_START_COORD; j <= Y_END_COORD; ++j) {
                if (
                    i >= 0 && i < areaWidth &&
                    j >= 0 && j < areaHeight &&
                    !(i == mainCellCoordinates.x && j == mainCellCoordinates.y)
                ) {
                    coords.add(new DiscreteCoordinates(i, j));
                }
            }
        }
    
        return coords;
    }

}
