package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.FIRE_WALL;
import static ch.epfl.cs107.icoop.actor.ElementalWall.WallType.WATER_WALL;
import static ch.epfl.cs107.icoop.actor.Orb.OrbType.FIRE_ORB;
import static ch.epfl.cs107.icoop.actor.Orb.OrbType.WATER_ORB;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

public class OrbWay extends ICoopArea {
    private DialogHandler dialogHandler;

    public OrbWay(DialogHandler handler){
        super();
        dialogHandler = handler;
    }

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1,5)};
    }

    @Override
    protected void createArea() {
        registerActor(new Orb(this,new DiscreteCoordinates(17,12), FIRE_ORB, dialogHandler));
        registerActor(new Orb(this,new DiscreteCoordinates(17,6), WATER_ORB, dialogHandler));
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(this, "Spawn", Logic.TRUE, new DiscreteCoordinates(18,16), new DiscreteCoordinates(18,15),
                new DiscreteCoordinates(0,14), new DiscreteCoordinates(0,13), new DiscreteCoordinates(0,12), new DiscreteCoordinates(0,11), new DiscreteCoordinates(0,10)));
        registerActor(new Door(this, "Spawn", Logic.TRUE, new DiscreteCoordinates(18,16), new DiscreteCoordinates(18,15),
                new DiscreteCoordinates(0,8), new DiscreteCoordinates(0,7), new DiscreteCoordinates(0,6), new DiscreteCoordinates(0,5), new DiscreteCoordinates(0,4)));

        registerActor(new Heart(this, Orientation.UP, new DiscreteCoordinates(8,4)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,6)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5,13)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,11)));

        PressurePlate fireWallPlate = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(5,7));
        PressurePlate waterWallPlate = new PressurePlate(this, Orientation.UP, new DiscreteCoordinates(5,10));

        registerActor(fireWallPlate);
        registerActor(waterWallPlate);

        for (int i=0; i<5; i++) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10+i), FIRE_WALL, fireWallPlate));
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4+i), WATER_WALL, waterWallPlate));
        }
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), WATER_WALL, Logic.FALSE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6) , FIRE_WALL, Logic.FALSE));

    }



    @Override
    public String getTitle() {
        return "OrbWay";
    }
}
