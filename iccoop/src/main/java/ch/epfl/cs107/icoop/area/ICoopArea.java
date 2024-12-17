package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.handler.AreaCellTypeHandler;
import ch.epfl.cs107.icoop.handler.Challenge;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area implements AreaCellTypeHandler, Logic {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;
    private Challenge challenge;

    /**
     * Area specific callback to initialise the instance
     */
    protected abstract void createArea();
    /**
     * @return the player's spawn position in the area
     */
    public abstract DiscreteCoordinates[] getPlayerSpawnPosition();

    /**
     * Callback to initialise the instance of the area
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the area is instantiated correctly, false otherwise
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            setBehavior(new ICoopBehavior(window, getTitle(), this));
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
    /**
     * Getter for Tuto2's scale factor
     * @return Scale factor in both the x-direction and the y-direction
     */
    @Override
    public final float getCameraScaleFactor() {
        return cameraScaleFactor;
    }

    public final float getDefaultCameraScaleFactor() {
        return DEFAULT_SCALE_FACTOR;
    }

    public void setCameraScaleFactor(float factor) {
        cameraScaleFactor = factor;
    }

    @Override
    public boolean isViewCentered () { return true ; }

    @Override
    public void addCellTypeActor(ICoopBehavior.ICoopCellType cellType, DiscreteCoordinates coords){
        switch (cellType) {
            case ROCK: {
                registerActor(new Rock(this, Orientation.DOWN, coords));
                break;
            }
            case OBSTACLE: {
                registerActor(new Obstacle(this, Orientation.DOWN, coords));
            }
        }
    }

    protected void createChallenge(Logic...signals){
        this.challenge = new Challenge(signals);
    }

    @Override
    public boolean isOn() {
        return challenge!=null && challenge.isOn();
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }
}
