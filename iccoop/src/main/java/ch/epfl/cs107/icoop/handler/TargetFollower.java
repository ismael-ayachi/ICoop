package ch.epfl.cs107.icoop.handler;


import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;

import static ch.epfl.cs107.play.math.Orientation.fromVector;

public interface TargetFollower  {

    default void targetDisplacement(TargetEntity target) {
        Vector followerVector = this.getCurrentMainCellCoordinates().toVector();
        Vector targetVector = target.getCurrentMainCellCoordinates().toVector();
        Vector v = targetVector.sub(followerVector);
        float deltaX = v.getX();
        float deltaY = v.getY();
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            orientate(fromVector(new Vector(deltaX,0)));
        }
        else if (Math.abs(deltaY) > Math.abs(deltaX)) {
            orientate(fromVector(new Vector(0, deltaY)));
        }                                
    }

    default boolean orientate(Orientation orientation) {return false;}

    default DiscreteCoordinates getCurrentMainCellCoordinates(){ return null;}
}
