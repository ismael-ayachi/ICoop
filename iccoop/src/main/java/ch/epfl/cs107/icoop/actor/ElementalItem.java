package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import org.w3c.dom.ls.LSOutput;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.List;

public class ElementalItem extends ICoopCollectable implements ElementalEntity,Logic {

    private Element type;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.type = element;

    }

    public Element getType() {
        return type;
    }



    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }


    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public boolean isOff() {
        return false;
    }

    @Override
    public Element element() {
        return this.type;
    }

}
