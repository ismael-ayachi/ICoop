package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.Sprite;

public enum ICoopItem implements InventoryItem {
    SWORD(new Sprite("sword.icon", 1f, 1f, null)),
    FIRE_KEY(new Sprite("key_red", 1f, 1f, null)),
    WATER_KEY(new Sprite("key_blue", 1f, 1f, null)),
    FIRE_STAFF(new Sprite("staff_fire.icon", 1f, 1f, null)),
    WATER_STAFF(new Sprite("staff_water.icon ", 1f, 1f, null)),
    BOMB(new Sprite("explosive", 1f, 1f, null)),;

    private final String name;
    private final Sprite sprite;

     ICoopItem(Sprite sprite) {
        this.sprite = sprite;
        this.name = sprite.getName();

    }

    @Override
    public int getPocketId() { return 0; }

    @Override
    public String getName() { return name; }

}
