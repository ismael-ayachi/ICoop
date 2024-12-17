package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends ElementalItem {

    private final Sprite sprite;

    public enum TypeKey {
        RED_KEY("icoop/key_red", Element.FIRE),
        BLUE_KEY("icoop/key_blue", Element.WATER);

        public final String keyName;
        public final Element element;

        TypeKey(String keyName, Element element) {
            this.keyName = keyName;
            this.element = element;
        }
    }


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
