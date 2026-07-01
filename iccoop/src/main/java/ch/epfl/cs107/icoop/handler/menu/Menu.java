package ch.epfl.cs107.icoop.handler.menu;

import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * Interface representing a generic menu in the game.
 * Provides methods for rendering the menu and handling user input.
 */
public interface Menu {
    /**
     * Draws the menu on the provided canvas.
     * @param canvas (Canvas): the canvas to render the menu on, not null.
     */
    void draw(Canvas canvas);
    /**
     * Handles user input actions through the keyboard.
     * @param keyboard (Keyboard): the keyboard to capture user input, not null.
     */
    void handleAction(Keyboard keyboard);
}