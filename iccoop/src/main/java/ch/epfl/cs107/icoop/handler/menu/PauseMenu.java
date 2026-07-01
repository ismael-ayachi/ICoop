package ch.epfl.cs107.icoop.handler.menu;

import ch.epfl.cs107.play.window.Keyboard;

/**
 * Represents the pause menu in the game.
 * Allows the user to choose between resuming the game or quitting.
 */
public class PauseMenu extends AbstractMenu {
    private boolean resumeSelected = false;
    private boolean quitSelected = false;
    private final GameState gameState;

    /**
     * Creates a new instance of the pause menu.
     * @param gameState (GameState): the state of the game being managed, not null.
     */
    public PauseMenu(GameState gameState) {
        super("PauseMenu", "PauseMenuResume", "PauseMenuQuit");
        this.gameState = gameState;
        currentState = 0;
    }

    /**
     * Checks if the "Resume" option is selected.
     * @return (boolean): true if "Resume" is selected, false otherwise.
     */
    public boolean isResumeSelected() {
        return resumeSelected;
    }

    /**
     * Checks if the "Quit" option is selected.
     * @return (boolean): true if "Quit" is selected, false otherwise.
     */
    public boolean isQuitSelected() {
        return quitSelected;
    }

    @Override
    public void handleAction(Keyboard keyboard) {
        if (keyboard.get(Keyboard.LEFT).isPressed()) {
            resumeSelected = true;
            quitSelected = false;
            currentState = 1;
        } else if (keyboard.get(Keyboard.RIGHT).isPressed()) {
            resumeSelected = false;
            quitSelected = true;
            currentState = 2;
        }
    }

}