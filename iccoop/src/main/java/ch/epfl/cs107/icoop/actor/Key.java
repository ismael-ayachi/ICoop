package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.MultipleAnd;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends ElementalItem implements Interactable{

    private final Element element;
    private final Sprite redKeySprite = new Sprite("icoop/key_red", 0.6f, 0.6f, this);
    private final Sprite blueKeySprite = new Sprite("icoop/key_blue", 0.6f, 0.6f, this);
    private KeyInteractionHandler handler;
    private Logic logicRedKey;
    private Logic logicBlueKey;

    private MultipleAnd RedKeyAndBlueKey = new MultipleAnd(logicRedKey, logicBlueKey);

    private int collectedKeys;
    public enum TypeKey {RED_KEY("icoop/key_red", Element.FIRE),
        BLUE_KEY("icoop/key_blue", Element.WATER);

        public final String keyname;
        public final Element element;

        TypeKey(String keyname, Element element) {
            this.keyname = keyname;
            this.element = element;
        }
    }


    public Key(Area area, Orientation orientation, DiscreteCoordinates position, TypeKey typekey) {
        super(area, orientation, position, typekey.element);
        this.element = typekey.element;
        handler = new KeyInteractionHandler();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()) {
            if (Element.FIRE.equals(element)) {
                redKeySprite.draw(canvas);
            }
            else if (Element.WATER.equals(element)) {
                blueKeySprite.draw(canvas);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Element.FIRE.equals(element) && isCollected()) {
            logicRedKey = Logic.TRUE;
        }
        else if (Element.WATER.equals(element) && isCollected()) {
            logicBlueKey = Logic.TRUE;
        }
        /*if (isCollected()) {
            collectedKeys++;


        }

         */
    }

    /*public void collectedKey() {
        if (isCollected()) {

            collectedKeys++;
        }
    }

     */



    public boolean isSameElement(ElementalEntity v) {
        return v.element() == element;
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

    /*
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

    }

     */

    @Override
    public Element element() {
        return element;
    }

    private class KeyInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Teleporter teleporter, boolean isCellInteraction) {
            System.out.println("test");
            if (RedKeyAndBlueKey.isOn()) {
                teleporter.isOn();
            }
        }
    }
}
