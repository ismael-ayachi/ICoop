package ch.epfl.cs107.icoop.handler.utilities;

import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Interface representing a handler for area cell types in the game.
 * Allows the addition of actors based on specific cell types and their coordinates.
 */
public interface AreaCellTypeHandler {
    /**
     * Adds an actor to the area based on the provided cell type and its coordinates.
     * Default implementation is provided but can be overridden by specific handlers.
     *
     * @param cellType (ICoopBehavior.ICoopCellType): Type of the cell, not null
     * @param coords (DiscreteCoordinates): Coordinates of the cell, not null
     */
    default void addCellTypeActor(ICoopBehavior.ICoopCellType cellType, DiscreteCoordinates coords){};
}
