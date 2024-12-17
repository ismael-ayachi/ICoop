package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public interface AreaCellTypeHandler {
    default void addCellTypeActor(ICoopBehavior.ICoopCellType cellType, DiscreteCoordinates coords){};
}
