package ch.epfl.cs107.icoop.handler.menu;

import ch.epfl.cs107.play.window.Keyboard;

/**
 * Represents the start menu of the game.
 * Allows the user to choose between starting the game or quitting.
 */
public class StartMenu extends AbstractMenu {
    private boolean startSelected = false;
    private boolean quitSelected = false;

    /**
     * Creates the start menu with default images for each state.
     */
    public StartMenu() {
        super("StartMenu", "StartMenuStartPressed", "StartMenuQuitPressed");
        currentState = 0;
    }

    /**
     * Checks if the "Start" option is selected.
     * @return (boolean): true if "Start" is selected, false otherwise.
     */
    public boolean isStartSelected() {
        return startSelected;
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
            startSelected = true;
            quitSelected = false;
            currentState = 1;
        } else if (keyboard.get(Keyboard.RIGHT).isPressed()) {
            startSelected = false;
            quitSelected = true;
            currentState = 2;
        }
    }
}