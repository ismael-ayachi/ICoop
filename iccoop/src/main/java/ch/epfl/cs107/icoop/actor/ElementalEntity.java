package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public interface ElementalEntity {
    Element element();
    enum Element {
        FIRE,WATER
    }
}
