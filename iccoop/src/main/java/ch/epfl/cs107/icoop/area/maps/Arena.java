package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.collectables.Key;
import ch.epfl.cs107.icoop.actor.entity.props.Teleporter;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

/**
 * Represents the "Arena" area in the game, where players and companions can spawn, collect keys,
 * and interact with a teleporter.
 */
public class Arena extends ICoopArea {

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(14, 15), new DiscreteCoordinates(4,5)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(14,16), new DiscreteCoordinates(4,6)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        /** Keys && Teleporter **/
        Key fireKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,16), Key.TypeKey.RED_KEY);
        Key waterKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,4), Key.TypeKey.BLUE_KEY);
        registerActor(fireKey);
        registerActor(waterKey);
        registerActor(new Teleporter(this, "Spawn", new DiscreteCoordinates(5, 8), new DiscreteCoordinates(7,8),
                new DiscreteCoordinates(5, 9), new DiscreteCoordinates(7,9), new DiscreteCoordinates(10,11), fireKey, waterKey));

        createChallenge(fireKey, waterKey);
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
