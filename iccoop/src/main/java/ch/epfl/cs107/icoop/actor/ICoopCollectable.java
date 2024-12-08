package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public boolean isCellInteractable() {
        if (isCollected()){
            return false;
        }
        else {
            return true;
        }
    }



    @Override
    public boolean takeCellSpace() {
        return true;
    }

    public boolean isViewInteractable(){
        return false;
    }



    public abstract void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction);

}


