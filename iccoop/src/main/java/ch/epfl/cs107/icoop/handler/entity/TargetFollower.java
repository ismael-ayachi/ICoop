package ch.epfl.cs107.icoop.handler.entity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;

import static ch.epfl.cs107.play.math.Orientation.fromVector;

/**
 * Interface representing a follower that adjusts its orientation and movement based on a target's position.
 */
public interface TargetFollower  {

    /**
     * Adjusts the orientation of the follower to face the target by calculating the shortest directional path.
     * The follower orients itself along the x-axis if the target is further away horizontally, or along the y-axis otherwise.
     * @param target (TargetEntity): the entity being followed. Not null.
     */
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

    /**
     * Sets the target entity for the follower to track.
     * @param target (TargetEntity): the entity to follow. Not null.
     */
    void setTarget(TargetEntity target);

    /**
     * Updates the orientation of the follower.
     * @param orientation (Orientation): the new orientation for the follower. Not null.
     * @return (boolean): true if the orientation was successfully updated, false otherwise.
     */
    boolean orientate(Orientation orientation);

    /**
     * Gets the current main cell coordinates of the follower.
     * @return (DiscreteCoordinates): the current cell position of the follower. Not null.
     */
    DiscreteCoordinates getCurrentMainCellCoordinates();
}
