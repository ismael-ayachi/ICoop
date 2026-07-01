package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.decor.DeadTree;
import ch.epfl.cs107.icoop.actor.entity.props.Bomb;
import ch.epfl.cs107.icoop.actor.entity.props.Chest;
import ch.epfl.cs107.icoop.actor.entity.props.Door;
import ch.epfl.cs107.icoop.actor.entity.props.ManorDoor;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.icoop.handler.item.ICoopItem;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents the "Spawn" area, which the main Area of the game where the players spawn.
 */
public class Spawn extends ICoopArea {
    private final DialogHandler dialogHandler;
    private final Dialog dialog;
    private boolean playDialog = true;

    /**
     * Constructor for the Spawn area.
     *
     * @param handler (DialogHandler): Dialog handler for managing dialogs, not null.
     * @param manorChallengeConditions (Logic...): Conditions for challenges in the manor.
     */
    public Spawn(DialogHandler handler, Logic...manorChallengeConditions){
        super();
        this.dialogHandler = handler;
        this.dialog = new Dialog("welcome");
        createChallenge(manorChallengeConditions);
    }

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(5, 8), new DiscreteCoordinates(7,8)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(5,9), new DiscreteCoordinates(7,9)};
    }


    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(13,11), ICoopItem.SWORD, 1,
                dialogHandler, "chest_spawn", "chest_required", Logic.TRUE ));
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(15,11), ICoopItem.SWORD, 1,
                dialogHandler, "chest_spawn", "chest_required", Logic.TRUE ));
        registerActor(new Door(this, "OrbWay", Logic.TRUE, new DiscreteCoordinates(1,12), new DiscreteCoordinates(1,6),
                new DiscreteCoordinates(1,13), new DiscreteCoordinates(1,7), new DiscreteCoordinates(19,15), new DiscreteCoordinates(19,16)));
        registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(11,10)));
        registerActor(new Door(this, "Maze", Logic.TRUE, new DiscreteCoordinates(2,38), new DiscreteCoordinates(3,38),
                new DiscreteCoordinates(2,39), new DiscreteCoordinates(3,39),new DiscreteCoordinates(4,0),  new DiscreteCoordinates(5,0)));

        registerActor(new ManorDoor(this, "SanctumEntrance", this, new DiscreteCoordinates(9,1), new DiscreteCoordinates(11,1),
                new DiscreteCoordinates(9,0), new DiscreteCoordinates(11,0), new DiscreteCoordinates(6,11), dialogHandler));

        registerActor(new DeadTree(this, Orientation.DOWN, new DiscreteCoordinates(13,6)));
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if (playDialog) {
            dialogHandler.publish(dialog);
            if (dialog.isCompleted()) {
                playDialog = false;
            }
        }
    }
}
