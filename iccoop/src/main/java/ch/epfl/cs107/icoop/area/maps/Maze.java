package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.entity.props.*;
import ch.epfl.cs107.icoop.actor.collectables.Heart;
import ch.epfl.cs107.icoop.actor.collectables.Staff;
import ch.epfl.cs107.icoop.actor.entity.foe.BombFoe;
import ch.epfl.cs107.icoop.actor.entity.foe.HellSkull;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.icoop.actor.entity.props.ElementalWall.WallType.FIRE_WALL;
import static ch.epfl.cs107.icoop.actor.entity.props.ElementalWall.WallType.WATER_WALL;
import static ch.epfl.cs107.icoop.actor.collectables.Staff.StaffType.WATER_STAFF;
import static ch.epfl.cs107.icoop.actor.collectables.Staff.StaffType.FIRE_STAFF;

/**
 * Represents the "Maze" area in the game, a challenging zone with obstacles, foes, and elemental puzzles.
 * This area includes interactive elements like doors, pressure plates, levers, and elemental walls, alongside foes and collectable items.
 */
public class Maze extends ICoopArea {

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(2, 38), new DiscreteCoordinates(3,38)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(2,39), new DiscreteCoordinates(3,39)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        /** Door **/

        registerActor(new Door(this, "Arena", Logic.TRUE, new DiscreteCoordinates(14,15), new DiscreteCoordinates(4,5),
                new DiscreteCoordinates(14,16), new DiscreteCoordinates(4,6), new DiscreteCoordinates(19,6), new DiscreteCoordinates(19,7)));

        /** Entities **/
        registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(6,25)));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15,18)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16,19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14,19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14,17)));

        /** Logic Signals**/
        PressurePlate pressurePlate1 = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(6,33));
        PressurePlate pressurePlate2 = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(9,25));
        registerActor(pressurePlate1);
        registerActor(pressurePlate2);
        Lever lever = new Lever(this, Orientation.DOWN, new DiscreteCoordinates(8,23));
        registerActor(lever);

        /** Walls **/
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,35), FIRE_WALL, pressurePlate1));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,36), FIRE_WALL, pressurePlate1));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,21), FIRE_WALL, pressurePlate2));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,35), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,36), WATER_WALL,Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2,34), FIRE_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(3,34), FIRE_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5,24), WATER_WALL, lever));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(6,24), WATER_WALL, lever));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,4), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13,4), FIRE_WALL, Logic.TRUE));

        /**Foes**/
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12,33)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12,31)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12,29)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12,27)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12,25)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10,33)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10,32)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10,30)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10,28)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10,26)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5,15)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5,14)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(6,17)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(10,17)));

        /**Staff**/
        Staff fireStaff = new Staff(this, new DiscreteCoordinates(13,2), FIRE_STAFF );
        Staff waterStaff = new Staff(this, new DiscreteCoordinates(8,2), WATER_STAFF );
        registerActor(fireStaff);
        registerActor(waterStaff);

        createChallenge(fireStaff, waterStaff);
    }

    @Override
    public String getTitle() {
        return "Maze";
    }
}
