package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Bomb;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Spawn extends ICoopArea {
    private final DialogHandler dialogHandler;
    private final Dialog dialog;
    private boolean playDialogue = true;

    public Spawn(DialogHandler handler){
        super();
        dialogHandler = handler;
        dialog = new Dialog("welcome");
    }

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14,6)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(this, "OrbWay", Logic.TRUE, new DiscreteCoordinates(1,12),
                new DiscreteCoordinates(1,5), new DiscreteCoordinates(19,15), new DiscreteCoordinates(19,16)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(10,10)));
        registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(11,10)));
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if (playDialogue) {
            dialogHandler.publish(dialog);
            if (dialog.isCompleted()) {
                playDialogue = false;
            }
        }
    }
}
