package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Door extends AreaEntity {

    private String destination;
    private Logic signal;
    private List<DiscreteCoordinates> playerDestination;
    private List<DiscreteCoordinates> position;

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    public Logic getSignal() {
        return signal;
    }

    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        this.destination = destination;
        this.signal = signal;
        playerDestination = Arrays.asList(posPlayer1, posPlayer2);
        this.position = List.of(position);
    }

    public Door(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates ... position) {
        super(area, Orientation.DOWN, position[0]);
        this.destination = destination;
        this.signal = signal;
        playerDestination = Arrays.asList(posPlayer1, posPlayer2);
        this.position = List.of(position);
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
    public void acceptInteraction ( AreaInteractionVisitor v , boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v). interactWith ( this ,isCellInteraction );
    }

    public void draw(Canvas canvas) {}



}

