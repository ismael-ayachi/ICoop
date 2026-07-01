package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.decor.Altar;
import ch.epfl.cs107.icoop.actor.entity.props.Mage;
import ch.epfl.cs107.icoop.actor.entity.foe.DarkLord;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents the "Sanctum" area in the game, an area where players must fight the final boss in
 * order to win the game.
 */
public class Sanctum extends ICoopArea {
    private final DialogHandler dialogHandler;

    public Sanctum(DialogHandler dialogHandler){
        super();
        this.dialogHandler = dialogHandler;
    }

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(7, 1), new DiscreteCoordinates(8,1)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8,0)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));

        registerActor(new Altar(this, Orientation.DOWN, new DiscreteCoordinates(4,6)));
        registerActor(new Altar(this, Orientation.DOWN, new DiscreteCoordinates(4,10)));
        registerActor(new Altar(this, Orientation.DOWN, new DiscreteCoordinates(11,6)));
        registerActor(new Altar(this, Orientation.DOWN, new DiscreteCoordinates(11,10)));

        DarkLord darkLord = new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(8,8));
        registerActor(darkLord);

        Mage magicMage = new Mage(this, Orientation.DOWN, new DiscreteCoordinates(7, 11), Mage.MageType.MAGIC,
                darkLord, dialogHandler);
        Mage fireMage = new Mage(this, Orientation.DOWN, new DiscreteCoordinates(5, 12), Mage.MageType.FIRE,
                darkLord, dialogHandler);
        Mage waterMage = new Mage(this, Orientation.DOWN, new DiscreteCoordinates(9, 12), Mage.MageType.WATER,
                darkLord, dialogHandler);

        registerActor(fireMage);
        registerActor(waterMage);
        registerActor(magicMage);

        createChallenge(fireMage, waterMage, magicMage);

    }

    @Override
    public String getTitle() {
        return "Sanctum";
    }
}

