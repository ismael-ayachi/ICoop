package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.engine.actor.Dialog;

import java.security.Key;


public final class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay"};
    private int areaIndex;

    private ICoopPlayer player1;
    private ICoopPlayer player2;

    private Dialog activeDialog;


    /**
     * Add all the Tuto2 areas
     */
    private void createAreas() {
        addArea(new Spawn(this));
        addArea(new OrbWay(this));
    }

    /**
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the game begins properly
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
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
        ICoopArea currentArea = (ICoopArea) getCurrentArea();
        CenterOfMass centerMassPlayers = new CenterOfMass(player1, player2);
        getCurrentArea().setViewCandidate(centerMassPlayers);
        float defaultFactor = currentArea.getDefaultCameraScaleFactor();
        float distance = (player1.getPosition().sub(player2.getPosition()).getLength()) / 2;
        float newFactor = Math.max(defaultFactor, (float) (defaultFactor * 0.75 + distance));
        currentArea.setCameraScaleFactor(newFactor);

        if (activeDialog == null) {
            super.update(deltaTime);

            if (keyboard.get(KeyBindings.RESET_GAME).isPressed()) {
                begin(getWindow(), getFileSystem());
            } else if (keyboard.get(KeyBindings.RESET_AREA).isPressed() || player1.isDead() || player2.isDead()) {
                resetArea();
            }

            if (player1.isDoorPassed()) {
                String areakey = player1.getCurrentDoor().getDestination();
                DiscreteCoordinates[] coords = player1.getCurrentDoor().getPlayerDestination();
                switchArea(areakey, coords, false);
            }
            player1.setDoorIsPassed(false);
            if (player2.isDoorPassed()) {
                String areakey = player2.getCurrentDoor().getDestination();
                DiscreteCoordinates[] coords = player2.getCurrentDoor().getPlayerDestination();
                switchArea(areakey, coords, false);
            }
            player2.setDoorIsPassed(false);

        } else {
            getCurrentArea().draw(getWindow());
            activeDialog.draw(getWindow());
            if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed() && !activeDialog.isCompleted()) {
                activeDialog.update(deltaTime);
            } else if (activeDialog.isCompleted()){
                activeDialog=null;
            }
        }
    }

    @Override
    public void publish(Dialog dialog) {
        this.activeDialog = dialog;
    }

    @Override
    public void end() {

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
        DiscreteCoordinates coords[] = area.getPlayerSpawnPosition();
        player1 = new ICoopPlayer(area, Orientation.DOWN, coords[0], ElementalEntity.Element.FIRE, "icoop/player");
        player2 = new ICoopPlayer(area, Orientation.DOWN, coords[1], ElementalEntity.Element.WATER, "icoop/player2");
        player1.enterArea(area, coords[0]);
        player2.enterArea(area, coords[1]);
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(String areaKey, DiscreteCoordinates[] coords, boolean reset) {
        player1.leaveArea();
        player2.leaveArea();
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areaKey, reset);
        player1.enterArea(currentArea, coords[0]);
        player2.enterArea(currentArea, coords[1]);
    }

    private void resetArea() {
        DiscreteCoordinates coords[] = ((ICoopArea) getCurrentArea()).getPlayerSpawnPosition();
        switchArea(getCurrentArea().getTitle(), coords, true);
        if (player1.isDead() || player2.isDead()) {
            player1.resetHealth();
            player2.resetHealth();
        }
    }


}