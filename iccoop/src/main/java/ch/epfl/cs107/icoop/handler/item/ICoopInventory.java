package ch.epfl.cs107.icoop.handler.item;

import ch.epfl.cs107.play.areagame.handler.Inventory;

/**
 * Represents an inventory system specific to the ICoop game, extending the generic Inventory class.
 * The inventory is associated with a named "pocket" to categorize items.
 */
public class ICoopInventory extends Inventory {
    /**
     * Constructs an inventory with a specific pocket name.
     * @param pocketName (String): the name of the inventory pocket, not null
     */
    public ICoopInventory(String pocketName) {
        super(pocketName);
    }
}
