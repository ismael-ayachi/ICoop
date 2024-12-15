package ch.epfl.cs107.icoop.handler;


import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;

import static ch.epfl.cs107.play.math.Orientation.fromVector;

public interface TargetFollower  {

    default boolean targetDisplacement(TargetEntity target) {
        //float distance = DiscreteCoordinates.distanceBetween(this.getCurrentMainCellCoordinates(), player.getCurrentMainCellCoordinates());
        float BombFoeX = this.getCurrentMainCellCoordinates().toVector().getX();
        float BombFoeY = this.getCurrentMainCellCoordinates().toVector().getY();
        float TargetX = target.getCurrentMainCellCoordinates().toVector().getX();
        float TargetY = target.getCurrentMainCellCoordinates().toVector().getY();
        Vector BombFoeVector = new Vector(BombFoeX, BombFoeY);
        Vector TargetVector = new Vector(TargetX, TargetY);
        Vector v = TargetVector.sub(BombFoeVector);
        float deltaX = v.getX();
        float deltaY = v.getY();
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            orientate(fromVector(new Vector(deltaX,0)));
            return false;
        }
        else if (Math.abs(deltaY) > Math.abs(deltaX)) {
            orientate(fromVector(new Vector(0, deltaY)));
            return false;
        }
        return true;
    }

    default boolean orientate(Orientation orientation) {return false;}

    default DiscreteCoordinates getCurrentMainCellCoordinates(){ return null;}
}
