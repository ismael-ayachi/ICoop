package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class ElementalWall extends AreaEntity implements ElementalEntity, Logic, Interactor {
    private final Sprite[] wallSprites;
    private Element element;
    private static final int DAMAGE_QUANTITY = 1;
    private ElementalWallInteractionHandler handler;

    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        wallSprites = RPGSprite.extractSprites (spriteName, 4, 1, 1,  this ,
                Vector.ZERO , 256 , 256);

        switch (spriteName) {
            case "fire_wall":
                element = Element.FIRE;
                break;
            case "water_wall":
                element = Element.WATER;
                break;
        }
        handler = new ElementalWallInteractionHandler();
    }


    @Override
    public Element element() {
        return element;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
        System.out.println("test");
    }


    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isOff() {
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        wallSprites[getOrientation().ordinal()].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true ;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        System.out.println("test1");
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
    private class ElementalWallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (element == Element.FIRE) {
                player.damage(DamageType.FIRE, DAMAGE_QUANTITY);
            }
            else if (element == Element.WATER) {
                player.damage(DamageType.WATER, DAMAGE_QUANTITY);
            }

        }
    }



}
