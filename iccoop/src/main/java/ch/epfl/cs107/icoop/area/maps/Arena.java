package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Arena extends ICoopArea {

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(14, 15), new DiscreteCoordinates(4,5)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        /** Keys && Teleporter **/
        Key redKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,16), Key.TypeKey.RED_KEY);
        Key blueKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,4), Key.TypeKey.BLUE_KEY);
        registerActor(redKey);
        registerActor(blueKey);
        registerActor(new Teleporter(this, "Spawn", new DiscreteCoordinates(5,5), new DiscreteCoordinates(7,5),
                new DiscreteCoordinates(10,11), redKey, blueKey));

        createChallenge(redKey, blueKey);
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
