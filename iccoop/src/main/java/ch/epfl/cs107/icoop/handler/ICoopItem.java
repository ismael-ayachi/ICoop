package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ICoopCollectable;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.Sprite;

public enum ICoopItem implements InventoryItem {
    SWORD("icoop/sword.icon"),
    FIRE_KEY("icoop/key_red"),
    WATER_KEY("icoop/key_blue"),
    FIRE_STAFF("icoop/staff_fire.icon"),
    WATER_STAFF("icoop/staff_water.icon "),
    BOMB("icoop/explosive");

    private final String spriteName;

     ICoopItem(String spriteName) {
        this.spriteName = spriteName;
    }

    @Override
    public int getPocketId() { return 0; }

    @Override
    public String getName() { return spriteName; }

}
