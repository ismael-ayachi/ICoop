package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Door extends AreaEntity {

    private String destination;
    private Logic signal;
    private DiscreteCoordinates[] playerDestination;
    private DiscreteCoordinates[] otherPositions;
    private DiscreteCoordinates mainPosition;


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        List <DiscreteCoordinates> cells = new ArrayList<>();
        Collections.addAll(cells, otherPositions);
        cells.add(mainPosition);
        return cells;

    }

    public Logic getSignal() {
        return signal;
    }

    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates mainPos) {
        super(area, Orientation.DOWN, mainPos);
        this.destination = destination;
        this.signal = signal;
        playerDestination = new DiscreteCoordinates[]{posPlayer1, posPlayer2};
        mainPosition = mainPos;
    }

    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2,DiscreteCoordinates mainPos, DiscreteCoordinates ... otherPos) {
        super(area, Orientation.DOWN, mainPos);
        this.destination = destination;
        this.signal = signal;
        playerDestination = new DiscreteCoordinates[]{posPlayer1, posPlayer2};
        mainPosition = mainPos;
        otherPositions = otherPos;
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

    @Override
    public boolean isCellInteractable() {
        return true;
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

