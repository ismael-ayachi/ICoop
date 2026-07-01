package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

/**
 * Represents an elemental orb collectible that provides specific abilities or messages upon collection.
 * The orb is animated and interacts with the player based on its element type.
 */
public class Orb extends ElementalItem {

    private final static int ANIMATION_DURATION = 24;
    private final static int ANIMATION_FRAMES = 6;
    final Sprite[] sprites = new Sprite [ ANIMATION_FRAMES ];
    private final Animation orbAnimation;

    private final Element element;
    private final DamageType damageType;

    private final Dialog orbDialog;
    private final DialogHandler dialogHandler;

    /**
     * Defines the types of orbs, their properties, and associated dialogs.
     */
    public enum OrbType {
        FIRE_ORB(64, Element.FIRE, DamageType.FIRE, new Dialog("orb_fire_msg")),
        WATER_ORB(0,Element.WATER, DamageType.WATER, new Dialog("orb_water_msg"));

        public final int spriteYDelta;
        public final Element element;
        public final DamageType damageType;
        public final Dialog orbDialog;

        /**
         * Creates an OrbType with specified properties.
         * @param spriteYDelta (int): Y-offset for sprite selection
         * @param element (Element): Element associated with the orb
         * @param damageType (DamageType): Damage type related to the orb
         * @param orbDialog (Dialog): Dialog to display upon collection
         */
        OrbType(int spriteYDelta, Element element, DamageType damageType, Dialog orbDialog) {
            this.spriteYDelta = spriteYDelta;
            this.element = element;
            this.orbDialog = orbDialog;
            this.damageType = damageType;
        }
    }
    
    /**
     * Creates an Orb with the specified properties and animation.
     * @param area (Area): The area where the orb is located, not null
     * @param position (DiscreteCoordinates): The position of the orb in the area, not null
     * @param orbType (OrbType): The type of the orb defining its properties
     * @param handler (DialogHandler): Handles dialog interactions for the orb
     */
    public Orb(Area area, DiscreteCoordinates position, OrbType orbType, DialogHandler handler) {
        super(area, Orientation.UP, position, orbType.element);
        this.element = orbType.element;
        this.damageType = orbType.damageType;
        this.orbDialog = orbType.orbDialog;

        this.dialogHandler = handler;


        for ( int i = 0; i < ANIMATION_FRAMES ; i ++) {
            sprites [i] = new RPGSprite("icoop/orb", 1, 1, this ,
                    new RegionOfInterest(i * 32 , orbType.spriteYDelta , 32 , 32) );
        }
        orbAnimation = new Animation ( ANIMATION_DURATION / ANIMATION_FRAMES , sprites );
    }

    /**
     * Gets the damage type associated with the orb.
     * @return (DamageType): The damage type of the orb
     */
    public DamageType getDamageType() {
        return damageType;
    }

    @Override
    public void collect() {
        dialogHandler.publish(orbDialog);
        super.collect();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        orbAnimation.draw(canvas);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        orbAnimation.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ElementalEntity){
            if(isSameElement((ElementalEntity) v)) {
                ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
            }
        }
    }

    @Override
    public Element element(){
        return element;
    }
}