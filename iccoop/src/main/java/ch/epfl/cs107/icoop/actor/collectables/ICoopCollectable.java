package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents collectable entities within the ICoop game, managing collection behavior
 * and interactions with other entities.
 */
public abstract class ICoopCollectable extends CollectableAreaEntity implements Logic {

    /**
     * Constructs a new collectable entity.
     * @param area (Area): the area where the entity is located, not null
     * @param orientation (Orientation): the orientation of the entity, not null
     * @param position (DiscreteCoordinates): the position of the entity, not null
     */
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

