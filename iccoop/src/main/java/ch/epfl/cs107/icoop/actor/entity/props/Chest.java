package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.item.ICoopItem;
import ch.epfl.cs107.icoop.handler.utilities.Challenge;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Chest holding items which can be opened by players under certain conditions.
 */
public class Chest extends AreaEntity {
    private final ICoopItem item;
    private final int itemQuantity;
    private boolean isOpened;
    private final DialogHandler dialogHandler;
    private final String openDialog;
    private final String errorDialog;

    private final Animation chestAnimation = new Animation("icoop/treasureChest", 2, 2, 2,
            this, 32, 32, new Vector(-.5f,0), 1, false);
    private final Challenge challenge;

    /**
     * Creates a Chest.
     * @param area (Area): the area where the chest is located
     * @param orientation (Orientation): the orientation of the chest
     * @param position (DiscreteCoordinates): the position of the chest
     * @param item (ICoopItem): the item contained within the chest
     * @param itemQuantity (int): the quantity of the item
     * @param dialogHandler (DialogHandler): the handler for dialogs
     * @param openDialog (String): the dialog displayed when the chest is opened
     * @param errorDialog (String): the dialog displayed when the chest cannot be opened
     * @param unlockConditions (Logic...): the conditions required to open the chest
     */
    public Chest(Area area, Orientation orientation, DiscreteCoordinates position, ICoopItem item, int itemQuantity, DialogHandler dialogHandler, String openDialog, String errorDialog, Logic... unlockConditions) {
        super(area, orientation, position);
        this.item = item;
        this.itemQuantity = itemQuantity;
        this.dialogHandler = dialogHandler;
        this.openDialog = openDialog;
        this.errorDialog = errorDialog;
        this.challenge = new Challenge(unlockConditions);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isOpened){
            chestAnimation.update(deltaTime);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

    @Override
    public void draw(Canvas canvas) {
        chestAnimation.draw(canvas);
    }

    /**
     * Attempts to open the chest if conditions are met.
     * If successful, the player receives the item and a dialog is shown.
     * If unsuccessful, an error dialog is displayed.
     * @param player (ICoopPlayer): the player attempting to open the chest
     */
    public void open(ICoopPlayer player) {
        if (!isOpened && challenge.isOn()) {
            isOpened = true;
            player.pickUpItem(item, itemQuantity);
            dialogHandler.publish(new Dialog (openDialog));
        } else if(!challenge.isOn()) {
            dialogHandler.publish(new Dialog(errorDialog));
        }
    }
}
