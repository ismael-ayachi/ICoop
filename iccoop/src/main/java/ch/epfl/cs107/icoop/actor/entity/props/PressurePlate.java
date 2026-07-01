package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
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
 * Represents a pressure plate in the game that reacts when a player steps on it.
 * The plate's state is determined by the presence of a player in its cell.
 */
public class PressurePlate extends AreaEntity implements Logic {
    private final Sprite sprite = new Sprite("GroundPlateOff", 1f, 1f, this);
    private ICoopPlayer currentPlayer;

    /**
     * Creates a PressurePlate in the specified area, orientation, and position.
     * @param area (Area): The area where the plate is located
     * @param orientation (Orientation): The orientation of the plate
     * @param position (DiscreteCoordinates): The position of the plate in the area
     */
    public PressurePlate(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        if(currentPlayer!=null) {
            return !currentPlayer.getCurrentMainCellCoordinates().equals(getCurrentMainCellCoordinates());
        }
        return true;
    }

    @Override
    public boolean isOff() {
        if (currentPlayer != null) {
            return currentPlayer.getCurrentMainCellCoordinates().equals(getCurrentMainCellCoordinates());
        }
        return false;
    }

    /**
     * Updates the plate's state when a player steps on it.
     * @param player (ICoopPlayer): The player stepping on the plate
     */
    public void step(ICoopPlayer player) {
        currentPlayer = player;
    }
}
