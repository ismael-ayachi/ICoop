package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;


public final class ICoop extends AreaGame {

    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player;
    private ICoopPlayer player2;
    private int areaIndex;


    /**
     * Add all the Tuto2 areas
     */
    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
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

        super.update(deltaTime);
        if (player.isDoorPassed()){
            String areakey = player.getCurrentDoor().getDestination();
            DiscreteCoordinates coords = player.getCurrentDoor().getPlayerDestination().get(0);
            switchArea(areakey, coords);
        }
        player.setDoorIsPassed(false);
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
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new ICoopPlayer(area, Orientation.DOWN, coords, ElementalEntity.Element.FIRE, "icoop/player");
        player2 = new ICoopPlayer(area, Orientation.DOWN, coords, ElementalEntity.Element.WATER, "icoop/player2");
        player.enterArea(area, coords);
        player.centerCamera();
        player2.enterArea(area, coords);
        player2.centerCamera();
    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(String areaKey, DiscreteCoordinates coords) {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areaKey, false);
        player.enterArea(currentArea, coords);
    }
}