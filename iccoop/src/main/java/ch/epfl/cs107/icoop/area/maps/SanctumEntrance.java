package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.entity.props.Chest;
import ch.epfl.cs107.icoop.actor.entity.props.Door;
import ch.epfl.cs107.icoop.actor.entity.foe.ElementalFoe;
import ch.epfl.cs107.icoop.actor.collectables.EvolutionPotion;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.utilities.DialogHandler;
import ch.epfl.cs107.icoop.handler.item.ICoopItem;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Represents the entrance area to the Sanctum. The area contains a door that connects to the Sanctum area
 */
public class SanctumEntrance extends ICoopArea {
    private final DialogHandler dialogHandler;

    public SanctumEntrance(DialogHandler dialogHandler){
        super();
        this.dialogHandler = dialogHandler;
    }


    @Override
    public DiscreteCoordinates[] getPlayerSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(9, 1), new DiscreteCoordinates(11,1)};
    }

    @Override
    public DiscreteCoordinates[] getCompanionSpawnPosition() {
        return new DiscreteCoordinates[]{new DiscreteCoordinates(9,0), new DiscreteCoordinates(11,0)};
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Door(this, "Sanctum", this, new DiscreteCoordinates(7, 1), new DiscreteCoordinates(8,1),
                new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8,0), new DiscreteCoordinates(9,15),
                new DiscreteCoordinates(10,15), new DiscreteCoordinates(11,15)));
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(2,3), ICoopItem.BOMB, 2,
                dialogHandler, "chest_sanctum", "chest_required", Logic.TRUE ));
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(4,3), ICoopItem.BOMB, 2,
                dialogHandler, "chest_sanctum", "chest_required", Logic.TRUE ));
        ElementalFoe elementalFoe = new ElementalFoe(this,Orientation.DOWN, new DiscreteCoordinates(10,10));
        registerActor(elementalFoe);

        EvolutionPotion redPotion = new EvolutionPotion(this, Orientation.DOWN, new DiscreteCoordinates(9, 12), elementalFoe, EvolutionPotion.PotionType.RED_POTION, dialogHandler);
        EvolutionPotion bluePotion = new EvolutionPotion(this, Orientation.DOWN, new DiscreteCoordinates(11, 12), elementalFoe, EvolutionPotion.PotionType.BLUE_POTION, dialogHandler);
        registerActor(redPotion);
        registerActor(bluePotion);


        createChallenge(elementalFoe, redPotion, bluePotion);
    }

    @Override
    public String getTitle() {
        return "SanctumEntrance";
    }
}


