package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.player.ManorDoorPlayerView;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents a Manor Door managing the player access and interactions based on a signal logic and dialog system.
 * The door can display dialogs depending on its state and the player's position.
 */
public class ManorDoor extends Door {
    private final DialogHandler dialogHandler;
    private boolean playDialog = true;
    private boolean playFinalDialog = true;
    private ManorDoorPlayerView player;
    
    /**
     * Constructs a ManorDoor instance with associated dialog and positional data.
     *
     * @param area (Area): The area where the door is located
     * @param destination (String): The destination linked to this door
     * @param signal (Logic): Logic signal controlling the door's state
     * @param posPlayer1 (DiscreteCoordinates): Position for the first player
     * @param posPlayer2 (DiscreteCoordinates): Position for the second player
     * @param posCompanionPlayer1 (DiscreteCoordinates): Companion's position for the first player
     * @param posCompanionPlayer2 (DiscreteCoordinates): Companion's position for the second player
     * @param doorPos (DiscreteCoordinates): Position of the door
     * @param handler (DialogHandler): The dialog handler managing the door's dialogs
     */
    public ManorDoor(Area area, String destination, Logic signal,  DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2,
                     DiscreteCoordinates posCompanionPlayer1, DiscreteCoordinates posCompanionPlayer2, DiscreteCoordinates doorPos, DialogHandler handler) {
        super(area, destination, signal, posPlayer1, posPlayer2, posCompanionPlayer1, posCompanionPlayer2, doorPos);
        this.dialogHandler = handler;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        playDialog = !(player != null && player.getCurrentMainCellCoordinates().equals(getCurrentMainCellCoordinates()));
    }

    /**
     * Publishes the appropriate dialog based on the door's state and logic signal.
     */
    public void publish() {
        if (playDialog) {
            if (isActive() && playFinalDialog) {
                dialogHandler.publish(new Dialog("victory"));
                playFinalDialog = false;
            } else if (!isActive()) {
                dialogHandler.publish(new Dialog("key_required"));
            }
        }
    }
    
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Sets the player view interacting with the door.
     * @param player (ManorDoorPlayerView): The player's view interacting with the door
     */
    public void setPlayerView(ManorDoorPlayerView player) {
        this.player = player;
    }
}
