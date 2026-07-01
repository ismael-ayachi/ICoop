package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a lever that can be toggled between activated and deactivated states.
 * The lever interacts visually and logically with other game entities.
 */
public class Lever extends AreaEntity implements Logic {
    private boolean activated = true;
    private final Sprite spriteUp = new Sprite("LeverUp", 1, 1, this);
    private final Sprite spriteDown = new Sprite("LeverDown", 1, 1, this);

    /**
     * Creates a Lever in the specified area, orientation, and position.
     * @param area (Area): The area where the lever is located, not null
     * @param orientation (Orientation): The initial orientation of the lever, not null
     * @param position (DiscreteCoordinates): The position of the lever in the area, not null
     */
    public Lever(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas){
        if(activated){
            spriteDown.draw(canvas);
        } else {
            spriteUp.draw(canvas);
        }
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        return activated;
    }

    @Override
    public boolean isOff() {
        return !activated;
    }
    /**
     * Toggles the state of the lever between activated and deactivated.
     */
    public void pull(){
        activated = !activated;
    }
}
