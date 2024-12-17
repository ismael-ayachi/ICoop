package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public abstract class ICoopCollectable extends CollectableAreaEntity implements Logic {

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public void collect(){
        super.collect();
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public boolean isCellInteractable() {
        return !isCollected();
    }

    @Override
    public boolean isViewInteractable(){
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        if(!isCollected()) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }

    @Override
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }
}

