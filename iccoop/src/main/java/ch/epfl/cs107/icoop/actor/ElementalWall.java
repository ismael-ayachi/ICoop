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

import java.util.Collections;
import java.util.List;

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactor {
    private final Sprite[] wallSprites;

    private final Element element;
    private final DamageType damageType;
    private static final int DAMAGE_QUANTITY = 2;

    private final ElementalWallInteractionHandler handler;

    private final Logic logicKey;

    public enum WallType {
        FIRE_WALL("fire_wall", Element.FIRE, DamageType.FIRE),
        WATER_WALL("water_wall",Element.WATER, DamageType.WATER);

        public final String spriteName;
        public final Element element;
        public final DamageType damageType;

        WallType(String spriteName, Element element, DamageType damageType) {
            this.spriteName = spriteName;
            this.element = element;
            this.damageType = damageType;
        }

        public void p(){
            for(WallType w : values()){}
        }
    }

    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, WallType wallType, Logic key) {
        super(area, orientation, position);
        wallSprites = RPGSprite.extractSprites (wallType.spriteName, 4, 1, 1,  this,
                Vector.ZERO , 256 , 256);
        this.element = wallType.element;
        this.damageType = wallType.damageType;
        this.logicKey = key;
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
        if(logicKey.isOn()) {
            other.acceptInteraction(handler, isCellInteraction);
        }
    }

    @Override
    public void draw(Canvas canvas){
        if(logicKey.isOn()) {
            super.draw(canvas);
            wallSprites[getOrientation().ordinal()].draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public boolean isDisabled(){
        return logicKey.isOff();
    }

    public void destroy() {getOwnerArea().unregisterActor(this);}

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
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
    private class ElementalWallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.damage(damageType, DAMAGE_QUANTITY);
        }
    }
}
