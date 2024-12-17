package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.MultipleAnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Door extends AreaEntity {

    private final String destination;
    private final Logic signal;
    private final DiscreteCoordinates[] playerDestination;
    private final DiscreteCoordinates[] doorPosition;

    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates ... doorPos) {
        super(area, Orientation.DOWN, doorPos[0]);
        this.destination = destination;
        this.signal = signal;
        this.playerDestination = new DiscreteCoordinates[]{posPlayer1, posPlayer2};
        this.doorPosition = doorPos;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        List <DiscreteCoordinates> cells = new ArrayList<>();
        Collections.addAll(cells, doorPosition);
        return cells;

    }

    public String getDestination() {
        return destination;
    }

    public DiscreteCoordinates[] getPlayerDestination() {
        return playerDestination;
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }


    public boolean isActive() {
        return signal.isOn();
    }


    @Override
    public boolean isCellInteractable() {
        return isActive();
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

}

