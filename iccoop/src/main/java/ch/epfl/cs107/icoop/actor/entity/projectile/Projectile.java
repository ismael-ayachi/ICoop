package ch.epfl.cs107.icoop.actor.entity.projectile;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Positionable;

import java.util.Collections;
import java.util.List;

/**
 * Represents a projectile moving in a straight line and interacts with its environment.
 * The projectile travels a maximum distance and is removed upon reaching its limit or interacting with an entity.
 */
public abstract class Projectile extends MovableAreaEntity implements Interactor, Positionable {

    private final int speed;
    private final int MAX_DISTANCE;
    private double distance;
    private static final int MOVE_DURATION = 8;

    /**
     * Creates a projectile with specified speed and maximum travel distance.
     * @param area (Area): The area where the projectile is located, not null
     * @param orientation (Orientation): The direction of the projectile's movement
     * @param position (DiscreteCoordinates): The initial position of the projectile, not null
     * @param speed (int): The speed of the projectile, must be greater than 0
     * @param maxDistance (int): The maximum distance the projectile can travel, must be greater than 0
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance) {
        super(area, orientation, position);
        this.speed = speed;
        this.MAX_DISTANCE = maxDistance;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isDisplacementOccurs()) {
            move(MOVE_DURATION/speed);
            travel();
        }
    }

    /**
     * Handles the projectile's movement and checks if it has reached its maximum distance.
     */
    public void travel() {
        move ( MOVE_DURATION/speed);
        if (distance < MAX_DISTANCE) {
            distance++;
        }
        else{
            stopProjectile();
        }
    }

    /**
     * Stops the projectile by unregistering it from the area.
     */
    public void stopProjectile() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean wantsCellInteraction(){
        return distance != MAX_DISTANCE;
    }

    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
