package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;


/**
 * Represents an EvolutionPotion that can evolve the player’s companion when collected.
 * The potion is linked to a signal, becoming active only when the signal is on.
 */
public class EvolutionPotion extends ElementalItem {

    private final Animation animation;
    private final Logic signal;
    private final DialogHandler dialogHandler;

    /**
     * Types of evolution potions with associated graphical and elemental properties.
     */
    public enum PotionType {
        RED_POTION("icoop/potion_red", Element.FIRE),
        BLUE_POTION("icoop/potion_blue", Element.WATER);

        public final String keyName;
        public final Element element;

        /**
         * Constructor for the PotionType.
         * @param keyName (String): Sprite path
         * @param element (Element): Associated element
         */
        PotionType(String keyName, Element element) {
            this.keyName = keyName;
            this.element = element;
        }
    }

    /**
     * Creates an EvolutionPotion.
     * @param area (Area): The area where the potion exists
     * @param orientation (Orientation): The orientation of the potion
     * @param position (DiscreteCoordinates): The position of the potion
     * @param signal (Logic): Signal determining if the potion is active
     * @param potionType (PotionType): Type of the potion (red or blue)
     * @param dialogHandler (DialogHandler): Handler for dialog interactions
     */
    public EvolutionPotion(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal, PotionType potionType, DialogHandler dialogHandler) {
        super(area, orientation, position, potionType.element);
        this.signal = signal;
        this.animation = new Animation(potionType.keyName, 7, .5f, 1, this,
                18, 35, new Vector(.5f,0), 8, true);
        this.dialogHandler= dialogHandler;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isCollected() && signal.isOn())
            animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected() && signal.isOn()) {
            animation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isCellInteractable(){
        return signal.isOn();
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
    public void collect(){
        dialogHandler.publish(new Dialog("evolution"));
        super.collect();
    }
}
