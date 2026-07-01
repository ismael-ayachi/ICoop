package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.entity.player.CenterOfMass;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.*;
import ch.epfl.cs107.icoop.handler.menu.*;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.SoundAcoustics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.engine.actor.Dialog;

import java.io.InputStream;

public final class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay", "Maze", "Arena", "SanctumEntrance", "Sanctum"};

    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private Dialog activeDialog;
    private final GameState gameState = new GameState();
    private Menu menuStart, menuPause;
    private boolean drawStartMenu;
    private Sanctum finalAreaChallenge;

    private final SoundAcoustics defaultMusic = new SoundAcoustics(ResourcePath.getSound("GameDefaultMusic"), 0.5f, true, false, true, false);

    private boolean isRunning = true;


    /**
     * Add all the areas
     */

    private void createAreas() {

        OrbWay orbWay = new OrbWay(this);
        Maze maze = new Maze();
        Arena arena = new Arena();
        Spawn spawn = new Spawn(this, orbWay, maze, arena);
        SanctumEntrance sanctumEntrance = new SanctumEntrance(this);
        Sanctum sanctum = new Sanctum(this);
        finalAreaChallenge = sanctum;

        
        addArea(arena);
        addArea(maze);
        addArea(spawn);
        addArea(orbWay);
        addArea(sanctumEntrance);
        addArea(sanctum);
    }

    /**3
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the game begins properly
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (window == null || fileSystem == null) {
            return false;
        }
        if (super.begin(window, fileSystem)) {
            SoundAcoustics.stopAllSounds(getWindow());
            createAreas();
            initArea(areas[0]);
            gameState.setPaused(false);
            menuStart = new StartMenu();
            menuPause = new PauseMenu(gameState);
            drawStartMenu = true;

            defaultMusic.shouldBeStarted();
            defaultMusic.bip(getWindow());


            return true;
        }
        return false;
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {

        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (!isRunning) {
            return;
        }

        if (drawStartMenu ) {

            menuStart.draw(getWindow());
            menuStart.handleAction(getCurrentArea().getKeyboard());
            if (((StartMenu) menuStart).isStartSelected()) {
                if (keyboard.get(Keyboard.ENTER).isPressed()) {
                    drawStartMenu = false;

                }
            } else if (((StartMenu) menuStart).isQuitSelected()) {
                if (keyboard.get(Keyboard.ENTER).isPressed()) {
                    end();
                }
            }
        }



        else {
            if (keyboard.get(Keyboard.ESCAPE).isPressed() && !gameState.isPaused()) {
                gameState.setPaused(true);
            }

            if (gameState.isPaused()) {
                menuPause.draw(getWindow());
                menuPause.handleAction(keyboard);

                if (((PauseMenu) menuPause).isResumeSelected()) {
                    if (keyboard.get(Keyboard.ENTER).isPressed()) {
                        gameState.setPaused(false);
                    }

                } else if (((PauseMenu) menuPause).isQuitSelected()) {
                    if (keyboard.get(Keyboard.ENTER).isPressed()) {
                        end();
                    }
                }
            }

            else {
                ICoopArea currentArea = (ICoopArea) getCurrentArea();
                CenterOfMass centerMassPlayers = new CenterOfMass(player1, player2);
                getCurrentArea().setViewCandidate(centerMassPlayers);
                float defaultFactor = currentArea.getDefaultCameraScaleFactor();
                float distance = (player1.getPosition().sub(player2.getPosition()).getLength()) / 1.3f;
                float newFactor = Math.max(defaultFactor, (float) (defaultFactor * 0.75 + distance));
                currentArea.setCameraScaleFactor(newFactor);

                if (activeDialog == null) {
                    if(finalAreaChallenge.isOn()) {
                        begin(getWindow(), getFileSystem());
                    }

                    if (player1.isDoorPassed()) {
                        String areaKey = player1.getCurrentDoor().getDestination();
                        DiscreteCoordinates[] coords = player1.getCurrentDoor().getPlayerDestination();
                        DiscreteCoordinates[] companionCoords = player1.getCurrentDoor().getCompanionDestination();
                        switchArea(areaKey, coords, companionCoords, false);
                    }
                    if (player2.isDoorPassed()) {
                        String areaKey = player2.getCurrentDoor().getDestination();
                        DiscreteCoordinates[] coords = player2.getCurrentDoor().getPlayerDestination();
                        DiscreteCoordinates[] companionCoords = player2.getCurrentDoor().getCompanionDestination();
                        switchArea(areaKey, coords, companionCoords, false);
                    }
                    super.update(deltaTime);

                    if (keyboard.get(KeyBindings.RESET_GAME).isPressed()) {
                        begin(getWindow(), getFileSystem());
                    } else if (keyboard.get(KeyBindings.RESET_AREA).isPressed() || player1.isDead() || player2.isDead()) {
                        resetArea();
                    }
                } else {
                    getCurrentArea().draw(getWindow());
                    activeDialog.draw(getWindow());
                    if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed() && !activeDialog.isCompleted()) {
                        activeDialog.update(deltaTime);
                    } else if (activeDialog.isCompleted()) {
                        activeDialog = null;
                    }
                }
            }
        }
    }

    @Override
    public void publish(Dialog dialog) {
        this.activeDialog = dialog;
    }

    @Override
    public void end() {
        super.end();
        isRunning = false;
        // getWindow().dispose();
        // getCurrentArea().suspend();

        if (defaultMusic != null) {
            defaultMusic.bip(null);
        }

        // Nettoyer les zones et joueurs
        if (player1 != null) {
            player1.leaveArea();
            player1 = null;
        }
        if (player2 != null) {
            player2.leaveArea();
            player2 = null;
        }


        getCurrentArea().suspend();
        if (getWindow() != null) {
            getWindow().dispose();
        }
    }



    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * sets the area named `areaKey` as current area in the game Tuto2
     * @param areaKey (String) title of an area
     */
    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates[] coords = area.getPlayerSpawnPosition();
        player1 = new ICoopPlayer(area, Orientation.DOWN, coords[0], ICoopPlayer.PlayerType.RED_PLAYER);
        player2 = new ICoopPlayer(area, Orientation.DOWN, coords[1], ICoopPlayer.PlayerType.BLUE_PLAYER);
        player1.enterArea(area, coords[0], null);
        player2.enterArea(area, coords[1], null);
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(String areaKey, DiscreteCoordinates[] coords, DiscreteCoordinates[] companionCoords, boolean reset) {
        player1.leaveArea();
        player2.leaveArea();
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areaKey, reset);
        player1.enterArea(currentArea, coords[0], companionCoords[0]);
        player2.enterArea(currentArea, coords[1], companionCoords[1]);
    }

    private void resetArea() {
        DiscreteCoordinates[] coords = ((ICoopArea) getCurrentArea()).getPlayerSpawnPosition();
        DiscreteCoordinates[] companionCoords = ((ICoopArea) getCurrentArea()).getCompanionSpawnPosition();
        switchArea(getCurrentArea().getTitle(), coords, companionCoords, true);
        if (player1.isDead() || player2.isDead()) {
            player1.resetHealth();
            player2.resetHealth();
        }
    }
}