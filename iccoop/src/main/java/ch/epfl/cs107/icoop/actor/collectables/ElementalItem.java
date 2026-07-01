package ch.epfl.cs107.icoop.actor.collectables;


import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

/**
 * Represents a collectable item with an elemental property.
 * Implements logic for comparing and retrieving its element.
 */
public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity {

    private final Element element;

    /**
     * Constructs an ElementalItem with the specified element.
     * @param area (Area): The area where the item is located, not null.
     * @param orientation (Orientation): The orientation of the item, not null.
     * @param position (DiscreteCoordinates): The position of the item, not null.
     * @param element (Element): The elemental property of the item, not null.
     */
    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;
    }

    /**
     * Checks if another ElementalEntity has the same element as this item.
     * @param v (ElementalEntity): the entity to compare with, not null.
     * @return (boolean): true if the elements match, false otherwise.
     */
    public boolean isSameElement(ElementalEntity v) {
        return v.element() == element;
    }

    @Override
    public Element element() {
        return element;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
    }
}
