package ch.epfl.cs107.icoop.handler.player;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Interface representing the player view for interacting with the ManorDoor.
 * Provides a method to retrieve the player's current position in discrete coordinates.
 */
public interface ManorDoorPlayerView {

    /**
     * Retrieves the current main cell coordinates of the player.
     * @return (DiscreteCoordinates): the current main cell coordinates of the player
     */
    DiscreteCoordinates getCurrentMainCellCoordinates();
}
