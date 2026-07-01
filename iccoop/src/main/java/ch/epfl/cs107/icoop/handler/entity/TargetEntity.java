package ch.epfl.cs107.icoop.handler.entity;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Represents an entity that can be targeted within the game.
 * A TargetEntity provides its current main cell coordinates, allowing other actors to locate it spatially.
 */
public interface TargetEntity {

    /**
     * Retrieves the current main cell coordinates of the target entity.
     * @return (DiscreteCoordinates) the coordinates of the cell where the entity is currently located, not null.
     */
    DiscreteCoordinates getCurrentMainCellCoordinates();
}
