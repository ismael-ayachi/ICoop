package ch.epfl.cs107.icoop.handler.item;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

/**
 * Enum representing the various collectible items in the ICoop game.
 * Each item is associated with a unique sprite for graphical representation.
 */
public enum ICoopItem implements InventoryItem {
    SWORD("icoop/sword.icon"),
    FIRE_KEY("icoop/key_red"),
    WATER_KEY("icoop/key_blue"),
    FIRE_STAFF("icoop/staff_fire.icon"),
    WATER_STAFF("icoop/staff_water.icon"),
    BOMB("icoop/explosive");

    private final String spriteName;

    /**
     * Constructs an ICoopItem with a specific sprite name.
     * @param spriteName (String): the name of the sprite associated with the item.
     */
    ICoopItem(String spriteName) {
        this.spriteName = spriteName;
    }

    @Override
    public int getPocketId() { return 0; }

    @Override
    public String getName() { return spriteName; }

}
