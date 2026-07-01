package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.entity.props.Door;
import ch.epfl.cs107.icoop.actor.entity.props.ElementalWall;
import ch.epfl.cs107.icoop.actor.collectables.Heart;
import ch.epfl.cs107.icoop.actor.collectables.Orb;
import ch.epfl.cs107.icoop.actor.entity.props.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.icoop.actor.entity.props.ElementalWall.WallType.FIRE_WALL;
import static ch.epfl.cs107.icoop.actor.entity.props.ElementalWall.WallType.WATER_WALL;
import static ch.epfl.cs107.icoop.actor.collectables.Orb.OrbType.FIRE_ORB;
import static ch.epfl.cs107.icoop.actor.collectables.Orb.OrbType.WATER_ORB;

/**
 * OrbWay defines a game area featuring orbs, walls, and other interactable entities.
 * Players must navigate the area to collect orbs and interact with pressure plates and walls.
 */
public class OrbWay extends ICoopArea {
    private final DialogHandler dialogHandler;

    /**
     * Constructs the OrbWay area with a dialog handler.
     * @param dialogHandler (DialogHandler): handles dialog interactions in the area
     */
    public OrbWay(DialogHandler dialogHandler){
        super();
        this.dialogHandler = dialogHandler;
    }

    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1,6)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(0,12), new DiscreteCoordinates(0,6)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        /**Doors**/
        registerActor(new Door(this, "Spawn", Logic.TRUE, new DiscreteCoordinates(17,16), new DiscreteCoordinates(17,15), new DiscreteCoordinates(18,16), new DiscreteCoordinates(18,15),
                new DiscreteCoordinates(0,14), new DiscreteCoordinates(0,13), new DiscreteCoordinates(0,12), new DiscreteCoordinates(0,11), new DiscreteCoordinates(0,10)));
        registerActor(new Door(this, "Spawn", Logic.TRUE, new DiscreteCoordinates(17,16), new DiscreteCoordinates(17,15), new DiscreteCoordinates(18,16), new DiscreteCoordinates(18,15),
                new DiscreteCoordinates(0,8), new DiscreteCoordinates(0,7), new DiscreteCoordinates(0,6), new DiscreteCoordinates(0,5), new DiscreteCoordinates(0,4)));

        /** Hearts **/
        registerActor(new Heart(this, Orientation.UP, new DiscreteCoordinates(8,4)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,6)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5,13)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,11)));

        /** Pressure Plates **/
        PressurePlate fireWallPlate = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(5,7));
        PressurePlate waterWallPlate = new PressurePlate(this, Orientation.UP, new DiscreteCoordinates(5,10));

        registerActor(fireWallPlate);
        registerActor(waterWallPlate);

        /** Walls **/
        for (int i=0; i<5; i++) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10+i), FIRE_WALL, fireWallPlate));
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4+i), WATER_WALL, waterWallPlate));
        }
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), WATER_WALL, Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6) , FIRE_WALL, Logic.TRUE));

        /** Orbs **/
        Orb fireOrb = new Orb(this,new DiscreteCoordinates(17,12), FIRE_ORB, dialogHandler);
        Orb waterOrb = new Orb(this,new DiscreteCoordinates(17,6), WATER_ORB, dialogHandler);
        registerActor(fireOrb);
        registerActor(waterOrb);

        createChallenge(fireOrb, waterOrb);
    }



    @Override
    public String getTitle() {
        return "OrbWay";
    }
}
