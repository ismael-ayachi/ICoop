package ch.epfl.cs107.icoop.handler.utilities;

import ch.epfl.cs107.play.engine.actor.Dialog;

/**
 * Interface for handling dialog interactions in the game.
 * This interface defines a method to publish a dialog.
 */
public interface DialogHandler {
    /**
     * Publishes the provided dialog to be displayed in the game.
     * @param dialog (Dialog): the dialog to be published, not null
     */
    void publish(Dialog dialog);
}
