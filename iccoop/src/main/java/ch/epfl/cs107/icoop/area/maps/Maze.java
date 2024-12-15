package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Bomb;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.Heart;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.FIRE_WALL;
import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.WATER_WALL;

public class Maze extends ICoopArea {


    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3,39)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        /** Entities **/
        registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(6,25)));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15,18)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16,19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14,19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14,17)));

        /** Pressure Plates**/
        PressurePlate pressurePlate1 = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(6,33));
        PressurePlate pressurePlate2 = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(9,25));

        registerActor(pressurePlate1);
        registerActor(pressurePlate2);

        /** Walls **/
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,35), FIRE_WALL, pressurePlate1));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,36), FIRE_WALL, pressurePlate1));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,21), FIRE_WALL, pressurePlate2));

        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,35), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,36), WATER_WALL,Logic.TRUE));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2,34), FIRE_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(3,34), FIRE_WALL, Logic.TRUE));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5,24), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(6,24), WATER_WALL, Logic.TRUE));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,4), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13,4), FIRE_WALL, Logic.TRUE));


    }

    @Override
    public String getTitle() {
        return "Maze";
    }
}
