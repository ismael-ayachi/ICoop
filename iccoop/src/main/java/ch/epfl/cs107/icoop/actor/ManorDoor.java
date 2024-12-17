package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ManorDoorPlayerView;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class ManorDoor extends Door{
    private final DialogHandler dialogHandler;
    private boolean playDialog = true;
    private boolean playFinalDialog = true;
    private ManorDoorPlayerView player;

    public ManorDoor(Area area, String destination, Logic signal,  DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates doorPos, DialogHandler handler) {
        super(area, destination, signal, posPlayer1, posPlayer2, doorPos);
        this.dialogHandler = handler;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        playDialog = !(player != null && player.getCurrentMainCellCoordinates().equals(getCurrentMainCellCoordinates()));
    }

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

    public void setPlayerView(ManorDoorPlayerView player) {
        this.player = player;
    }
}
