package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.FIRE_WALL;
import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.WATER_WALL;
import static ch.epfl.cs107.icoop.actor.Staff.StaffType.FIRE_STAFF;
import static ch.epfl.cs107.icoop.actor.Staff.StaffType.WATER_STAFF;

public class Arena extends ICoopArea {

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14,15)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        /** Keys **/
        registerActor(new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,16), Key.TypeKey.RED_KEY));
        registerActor(new Key(this, Orientation.DOWN, new DiscreteCoordinates(9,4), Key.TypeKey.BLUE_KEY));
        registerActor(new Teleporter(this, "Spawn", Logic.FALSE, new DiscreteCoordinates(5,5), new DiscreteCoordinates(7,5), new DiscreteCoordinates(10,10) ) );

        /** Rocks **/
        for (int i=1; i<getWidth()-2; i++ ) {
            registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(i,1)));
            registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(getWidth()-2,i)));
        }
        for (int i=1; i<getHeight()-1; i++ ) {
            registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(1,i)));
            registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(i,getHeight()-2)));
           
        }



    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
