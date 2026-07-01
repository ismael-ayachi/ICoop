package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a collectible key that can be associated with specific elements (FIRE or WATER).
 * The key can interact with other entities if their elements match.
 */
public class Key extends ElementalItem {

    private final Sprite sprite;

    /**
     * Enum defining the types of keys and their associated elements.
     */
    public enum TypeKey {
        RED_KEY("icoop/key_red", Element.FIRE),
        BLUE_KEY("icoop/key_blue", Element.WATER);

        public final String keyName;
        public final Element element;

        /**
         * Creates a TypeKey with a sprite name and an associated element.
         * @param keyName (String): Sprite name for the key
         * @param element (Element): Element associated with the key
         */
        TypeKey(String keyName, Element element) {
            this.keyName = keyName;
            this.element = element;
        }
    }

    /**
     * Creates a Key with a specific type and position in the area.
     * @param area (Area): The area where the key is located, not null
     * @param orientation (Orientation): Initial orientation of the key, not null
     * @param position (DiscreteCoordinates): Initial position of the key, not null
     * @param keyType (TypeKey): The type of the key with its associated element
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, TypeKey keyType) {
        super(area, orientation, position, keyType.element);
        this.sprite = new Sprite(keyType.keyName, 0.6f, 0.6f, this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()) {
            sprite.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ElementalEntity){
            if(isSameElement((ElementalEntity) v)) {
                ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
            }
        }
    }


}
