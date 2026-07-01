package ch.epfl.cs107.icoop.handler.menu;

/**
 * GameState manages the state of the game, including whether it is paused or over.
 */
public class GameState {
    private boolean paused = false;
    private boolean gameOver = false;

    /**
     * Checks if the game is paused.
     * @return (boolean): true if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the paused state of the game.
     * @param paused (boolean): true to pause the game, false to unpause it.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

}