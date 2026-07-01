package ch.epfl.cs107.icoop.actor.elemental;

/**
 * Interface for entities with an elemental property.
 */
public interface ElementalEntity {
    /**
     * Gets the entity's element.
     * @return (Element): the element.
     */
    Element element();
    /**
     * Enum for possible elements.
     */
    enum Element {
        FIRE, WATER, MAGIC, NO_ELEMENT
    }
}
