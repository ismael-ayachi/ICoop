package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public interface TargetEntity {
    default DiscreteCoordinates getCurrentMainCellCoordinates() {return null;}
}
