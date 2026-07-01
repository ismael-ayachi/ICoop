package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
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

/**
 * Represents a wall entity with elemental properties, capable of interacting with other entities based on a logic key.
 */
public class ElementalWall extends AreaEntity implements ElementalEntity, Interactor {
    private final Sprite[] wallSprites;

    private final Element element;
    private final DamageType damageType;
    private static final int DAMAGE_QUANTITY = 2;

    private final ElementalWallInteractionHandler handler;

    private final Logic logicKey;

    /**
     * Defines types of elemental walls with their properties.
     */
    public enum WallType {
        FIRE_WALL("fire_wall", Element.FIRE, DamageType.FIRE),
        WATER_WALL("water_wall",Element.WATER, DamageType.WATER);

        public final String spriteName;
        public final Element element;
        public final DamageType damageType;

        /**
         * Creates a WallType with associated properties.
         * @param spriteName (String): the name of the sprite.
         * @param element (Element): the elemental property.
         * @param damageType (DamageType): the type of damage caused.
         */
        WallType(String spriteName, Element element, DamageType damageType) {
            this.spriteName = spriteName;
            this.element = element;
            this.damageType = damageType;
        }

        public void p(){
            for(WallType w : values()){}
        }
    }

    /**
     * Creates an ElementalWall with a specific type and logic key.
     * @param area (Area): the area where the wall is located, not null.
     * @param orientation (Orientation): the orientation of the wall, not null.
     * @param position (DiscreteCoordinates): the position of the wall, not null.
     * @param wallType (WallType): the type of the wall, defining its behavior.
     * @param key (Logic): the logic key controlling the wall.
     */
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

    /**
     * Checks if the wall is disabled based on its logic key.
     * @return (boolean): true if the wall is disabled, false otherwise.
     */
    public boolean isDisabled(){
        return logicKey.isOff();
    }

    /**
     * Destroys the wall by unregistering it from the area.
     */
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

    /**
     * Handles interactions with the wall.
     */
    private class ElementalWallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.damage(damageType, DAMAGE_QUANTITY);
        }
    }
}
