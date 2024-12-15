package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.handler.*;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public final class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Inventory.Holder, TargetEntity {
    private final Element element;
    private DamageType immunity;

    private final static int MAX_LIFE = 5;
    private final Health hp;
    private final static int IFRAMES = 24;
    private final Timer invincibilityTimer;

    private final static int ANIMATION_DURATION = 4;
    private final static int MOVE_DURATION = 8;
    private final ICoopPlayerStatusGUI statusGUI;

    private final Vector anchor = new Vector(0, 0);
    private final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private final OrientedAnimation animation;
    private KeyBindings.PlayerKeyBindings keys = KeyBindings.RED_PLAYER_KEY_BINDINGS;

    private final ICoopPlayerInteractionHandler handler;

    private final ICoopInventory inventory;
    private ICoopItem currentItem;
    private int currentItemIndex;

    private Door currentDoor;
    private boolean doorIsPassed;

    /**
     * @param owner       (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     *                    //* @param spriteName (String) name of the sprite used as graphical representation
     */

    public enum PlayerType {
        RED_PLAYER("icoop/player", Element.FIRE, KeyBindings.RED_PLAYER_KEY_BINDINGS, false),
        BLUE_PLAYER("icoop/player2", Element.WATER, KeyBindings.BLUE_PLAYER_KEY_BINDINGS, true);

        public final String prefix;
        public final Element element;
        public final KeyBindings.PlayerKeyBindings keys;
        public final boolean posGUI;


        PlayerType(String prefix, Element element, KeyBindings.PlayerKeyBindings keys, boolean posGUI){
            this.prefix = prefix;
            this.element = element;
            this.keys = keys;
            this.posGUI = posGUI;
        }
    }

    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, PlayerType playerType) {
        super(owner, orientation, coordinates);
        this.element = playerType.element;
        this.keys = playerType.keys;
        this.animation = new OrientedAnimation(playerType.prefix, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32, true);
        this.hp = new Health(this,Transform.I.translated(0,1.75f),MAX_LIFE,true);

        this.invincibilityTimer = new Timer();

        this.handler = new ICoopPlayerInteractionHandler();

        this.inventory= new ICoopInventory("playerPocket");
        this.statusGUI = new ICoopPlayerStatusGUI(playerType.posGUI);

        inventory.addPocketItem(ICoopItem.SWORD, 1);
        currentItem = ICoopItem.SWORD;
        inventory.addPocketItem(ICoopItem.BOMB, 1);

        resetMotion();
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        }
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));

        invincibilityTimer.tick();

        if (keyboard.get(keys.switchItem()).isPressed()){
            switchItem();
        } else if (keyboard.get(keys.useItem()).isPressed()){
            useCurrentItem();
        }
        statusGUI.setCurrentItem(currentItem);
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        if (invincibilityTimer.interval(3)) {
            animation.draw(canvas);
        }
        hp.draw(canvas);
        statusGUI.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (keyboard.get(keys.useItem()).isPressed()) {
            return true;
        }
        return false;

    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }

    public void setCurrentDoor(Door door){
        this.currentDoor = door;
    }

    public Door getCurrentDoor(){
        return currentDoor;
    }

    public void setDoorIsPassed(boolean doorIsPassed){
        this.doorIsPassed=doorIsPassed;
    }

    public boolean isDoorPassed(){
        return doorIsPassed;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * makes the player entering a given area
     *
     * @param area     (Area):  the area to be entered, not null
     * @param position (DiscreteCoordinates): initial position in the entered area, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);

        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Center the camera on the player
     */

    @Override
    public ElementalEntity.Element element() {
        return this.element;
    }

    public void resetHealth() {
        hp.resetHealth();
    }

    public boolean invincible() {
        return invincibilityTimer.isGoing();
    }

    public void damage(DamageType damageType, int damage) {
        if (damageType!=immunity && !invincible() && hp.isOn()) {
            invincibilityTimer.setTime(IFRAMES);
            hp.decrease(damage);
        }
    }

    public boolean isDead() {
        return hp.isOff();
    }


    @Override
    public boolean possess(InventoryItem item){
        return inventory.contains(item);
    }

    public void switchItem() {
        for (int i=0; i<ICoopItem.values().length; i++) {
            currentItemIndex++;
            currentItemIndex %= ICoopItem.values().length;
            if (possess(ICoopItem.values()[currentItemIndex])) break;
        }
        currentItem = ICoopItem.values()[currentItemIndex];
    }

    public void useCurrentItem(){
        if (currentItem!=null) {
            if (possess(currentItem)) {
                switch (currentItem) {
                    case BOMB: {
                        if (placeBomb()) inventory.removePocketItem(currentItem, 1);
                    }
                    default : break;
                }
            }
            if (!possess(currentItem)) currentItem = null;
        }
    }

    public boolean placeBomb() {
        Bomb placedBomb = new Bomb(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst(), Bomb.DEFAULT_BOMB_TIMER);
        if (getOwnerArea().canEnterAreaCells(placedBomb, getFieldOfViewCells())) {
            return getOwnerArea().registerActor(placedBomb);
        }
        return false;
    }

    public ICoopItem getCurrentItem() {
        return currentItem;
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor, ElementalEntity {

        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn() && isCellInteraction) {
                setCurrentDoor(door);
                setDoorIsPassed(true);
            }
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            if (isCellInteraction) {
                inventory.addPocketItem(ICoopItem.BOMB,1);
                bomb.collect();
            } else {
                bomb.activate();
            }
        }

        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (isCellInteraction) {
                orb.collect();
                immunity = orb.getDamageType();
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            if (isCellInteraction) {
                heart.collect();
                hp.increase(1);
            }
        }

        @Override
        public void interactWith(PressurePlate plate, boolean isCellInteraction) {
            if (isCellInteraction) {
                plate.step(ICoopPlayer.this);
            }
        }

        @Override
        public Element element() {
            return element;
        }
    }
}

