package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * Represents a static, non-interactable dead tree occupying multiple cells in an area.
 * Handles its own animation and rendering.
 */
public class Mage extends AreaEntity implements Logic {

    private final Animation animation;
    private final MageType type;
    private final Logic signal;
    private final DialogHandler dialogHandler;
    private boolean publishedDialog;

    public enum MageType{
        MAGIC("icoop/mage_magic", "mage_magic"),WATER("icoop/mage_water","mage_water"),FIRE("icoop/mage_fire","mage_fire");

        public final String name;
        public final String dialogName;

        MageType(String name, String dialogName){
            this.name = name;
            this.dialogName = dialogName;
        }

    }

    /**
     * Creates a DeadTree at a specific position and orientation in the given area.
     * @param area (Area): the area where the tree is located, not null.
     * @param orientation (Orientation): the orientation of the tree, not null.
     * @param position (DiscreteCoordinates): the main cell position of the tree, not null.
     */
    public Mage(Area area, Orientation orientation, DiscreteCoordinates position, MageType type, Logic signal, DialogHandler dialogHandler) {
        super(area, orientation, position);
        this.type = type;
        this.signal = signal;
        this.dialogHandler = dialogHandler;
        animation = new Animation(type.name, 14, 3,3,this,64,64,
                new Vector(-.5f,0), 4, true);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates(), getCurrentMainCellCoordinates().jump(1,0));
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if(signal.isOn()) {
            animation.update(deltaTime);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        if(signal.isOn())
            animation.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return signal.isOn();
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return signal.isOn();
    }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

    @Override
    public boolean isOn(){
        return publishedDialog;
    }

    @Override
    public boolean isOff(){
        return !isOn();
    }

    public void publish(){
        if(!publishedDialog){
            dialogHandler.publish(new Dialog(type.dialogName));
            publishedDialog = true;

        }
    }
}