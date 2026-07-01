package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

public interface ManorDoorPlayerView {

    default DiscreteCoordinates getCurrentMainCellCoordinates() { return null;}
}
