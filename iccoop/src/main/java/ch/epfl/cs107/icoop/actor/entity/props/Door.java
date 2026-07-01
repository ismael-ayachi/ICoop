package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a door allowing players and their companions to travel between areas.
 */
public class Door extends AreaEntity {

    /**
     * The destination area this door leads to
     */
    private final String destination;

    /**
     * The logic signal that determines whether the door is active or not
     */
    private final Logic signal;

    /**
     * The positions where players will appear in the destination area
     */
    private final DiscreteCoordinates[] playerDestination;

    private final DiscreteCoordinates[] companionDestination;
    /**
     * The coordinates occupied by the door in the current area
     */
    private final DiscreteCoordinates[] doorPosition;

    /**
     * Constructor for the Door class.
     * @param area (Area): the area to which this door belongs
     * @param destination (String): the destination area name, not null
     * @param signal (Logic): the signal determining if the door is active, not null
     * @param posPlayer1 (DiscreteCoordinates): the destination position of player 1, not null
     * @param posPlayer2 (DiscreteCoordinates): the destination position of player 2, not null
     * @param doorPos (DiscreteCoordinates...): the positions occupied by the door, not null
     */
    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2,
                DiscreteCoordinates posCompanionPlayer1, DiscreteCoordinates posCompanionPlayer2, DiscreteCoordinates ... doorPos) {
        super(area, Orientation.DOWN, doorPos[0]);
        this.destination = destination;
        this.signal = signal;
        this.playerDestination = new DiscreteCoordinates[]{posPlayer1, posPlayer2};
        this.companionDestination = new DiscreteCoordinates[]{posCompanionPlayer1, posCompanionPlayer2};
        this.doorPosition = doorPos;
    }

    /**
     * Returns the cells occupied by the door in the current area.
     * @return (List<DiscreteCoordinates>): a list of discrete coordinates
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        List <DiscreteCoordinates> cells = new ArrayList<>();
        Collections.addAll(cells, doorPosition);
        return cells;
    }

    /**
     * Gets the destination area name of the door.
     * @return (String): the destination area name
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Gets the player destination coordinates.
     * @return (DiscreteCoordinates[]): an array of coordinates for players in the destination area
     */
    public DiscreteCoordinates[] getPlayerDestination() {
        return playerDestination;
    }

    public DiscreteCoordinates[] getCompanionDestination() {
        return companionDestination;
    }

    /**
     * Determines whether the door occupies space in its cell.
     * @return (boolean): always false as the door does not take cell space
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Checks if the door is active based on the signal.
     * @return (boolean): true if the signal is on, false otherwise
     */
    public boolean isActive() {
        return signal.isOn();
    }

    /**
     * Determines whether the door can be interacted with at the cell level.
     * @return (boolean): true if the door is active, false otherwise
     */
    @Override
    public boolean isCellInteractable() {
        return isActive();
    }

    /**
     * Determines whether the door can be interacted with at a distance (view level).
     * @return (boolean): always false as the door is not view interactable
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Accepts an interaction from a visitor.
     * @param v (AreaInteractionVisitor): the visitor interacting with the door
     * @param isCellInteraction (boolean): true if the interaction is at the cell level
     */
    @Override
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

}
