package ch.epfl.cs107.icoop.handler.entity;

import ch.epfl.cs107.icoop.actor.entity.projectile.ElementalBall;
import ch.epfl.cs107.icoop.actor.entity.props.*;
import ch.epfl.cs107.icoop.actor.collectables.*;
import ch.epfl.cs107.icoop.actor.decor.*;
import ch.epfl.cs107.icoop.actor.entity.foe.BombFoe;
import ch.epfl.cs107.icoop.actor.entity.foe.ElementalFoe;
import ch.epfl.cs107.icoop.actor.entity.foe.Foe;
import ch.epfl.cs107.icoop.actor.entity.foe.HellSkull;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopCompanion;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.entity.projectile.Projectile;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */
public interface ICoopInteractionVisitor extends AreaInteractionVisitor {

    /**
     * Interact with a specific cell in the ICoop behavior.
     * @param cell (ICoopBehavior.ICoopCell): the cell to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}

    /**
     * Interact with an ICoop player.
     * @param player (ICoopPlayer): the player to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}

    /**
     * Interact with a Door.
     * @param door (Door): the door to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Door door, boolean isCellInteraction) {}


    /**
     * Interact with a Bomb.
     * @param bomb (Bomb): the bomb to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Bomb bomb, boolean isCellInteraction) {}

    /**
     * Interact with an Obstacle.
     * @param obstacle (Obstacle): the obstacle to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {}
    /**
     * Interact with a Rock.
     * @param rock (Rock): the rock to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Rock rock, boolean isCellInteraction) {}

    /**
     * Interact with an ElementalWall.
     * @param elementalWall (ElementalWall): the elemental wall to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {}

    /**
     * Interact with an Orb.
     * @param orb (Orb): the orb to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Orb orb, boolean isCellInteraction) {}

    /**
     * Interact with a Heart.
     * @param heart (Heart): the heart to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Heart heart, boolean isCellInteraction) {}

    /**
     * Interact with a PressurePlate.
     * @param plate (PressurePlate): the pressure plate to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(PressurePlate plate, boolean isCellInteraction) {}

    /**
     * Interact with a Foe.
     * @param foe (Foe): the foe to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Foe foe, boolean isCellInteraction) {}

    /**
     * Interact with a HellSkull.
     * @param hellSkull (HellSkull): the hell skull to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(HellSkull hellSkull, boolean isCellInteraction) {}

    /**
     * Interact with a BombFoe.
     * @param bombFoe (BombFoe): the bomb foe to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(BombFoe bombFoe, boolean isCellInteraction) {}

    /**
     * Interact with a Staff.
     * @param staff (Staff): the staff to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Staff staff, boolean isCellInteraction) {}

    /**
     * Interact with a Projectile.
     * @param projectile (Projectile): the projectile to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Projectile projectile, boolean isCellInteraction) {}

    /**
     * Interact with a Key.
     * @param key (Key): the key to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Key key, boolean isCellInteraction) {}

    /**
     * Interact with a Teleporter.
     * @param teleporter (Teleporter): the teleporter to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Teleporter teleporter, boolean isCellInteraction) {}

    /**
     * Interact with an ElementalBall.
     * @param ball (ElementalBall): the elemental ball to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ElementalBall ball, boolean isCellInteraction) {}

    /**
     * Interact with a ManorDoor.
     * @param manorDoor (ManorDoor): the manor door to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ManorDoor manorDoor, boolean isCellInteraction) {}

    /**
     * Interact with an ElementalFoe.
     * @param elementalFoe (ElementalFoe): the elemental foe to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ElementalFoe elementalFoe, boolean isCellInteraction) {}

    /**
     * Interact with Grass.
     * @param grass (Grass): the grass to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Grass grass, boolean isCellInteraction) {}

    /**
     * Interact with a DeadTree.
     * @param tree (DeadTree): the dead tree to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(DeadTree tree, boolean isCellInteraction) {}

    /**
     * Interact with a Companion.
     * @param companion (ICoopCompanion): the companion to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(ICoopCompanion companion, boolean isCellInteraction) {}

    /**
     * Interact with a Chest.
     * @param chest (Chest): the chest to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Chest chest, boolean isCellInteraction) {}

    /**
     * Interact with a Lever.
     * @param lever (Lever): the lever to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Lever lever, boolean isCellInteraction) {}
    /**
     * Interact with an EvolutionPotion.
     * @param evolutionPotion (EvolutionPotion): the potion to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(EvolutionPotion evolutionPotion, boolean isCellInteraction) {}

    /**
     * Interact with an Altar.
     * @param altar (Altar): the altar to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Altar altar, boolean isCellInteraction) {}

    /**
     * Interact with a Mage.
     * @param mage (Mage): the mage to interact with.
     * @param isCellInteraction (boolean): true if the interaction is cell-based.
     */
    default void interactWith(Mage mage, boolean isCellInteraction) {}
}
