package ch.epfl.cs107.icoop.actor;


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.List;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity,Logic {

    private Element element;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;
    }

    public boolean isSameElement(AreaInteractionVisitor v) {
        return ((ICoopInteractionVisitor) v).element().equals(element);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
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
