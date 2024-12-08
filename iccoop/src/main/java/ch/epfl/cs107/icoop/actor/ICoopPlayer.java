package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public final class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Interactable {
    private final Element element;
    public final DamageType immunity = DamageType.WATER;

    private final static int MAX_LIFE = 5;
    private Health hp;
    private final static int IFRAMES = 24;
    private int timer;

    private final static int ANIMATION_DURATION = 4;
    private final static int MOVE_DURATION = 8;

    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private OrientedAnimation animation;
    private KeyBindings.PlayerKeyBindings keys = KeyBindings.RED_PLAYER_KEY_BINDINGS;

    private ICoopPlayerInteractionHandler handler;

    private Door currentDoor;
    private boolean doorIsPassed;

    /**
     * @param owner       (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     *                    //* @param spriteName (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element element, String prefix) {
        super(owner, orientation, coordinates);
        this.element = element;
        if (prefix == "icoop/player") {
            keys = KeyBindings.RED_PLAYER_KEY_BINDINGS;
        } else if (prefix == "icoop/player2") {
            keys = KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
        }
        animation = new OrientedAnimation(prefix, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32,
                true);
        resetMotion();
        handler = new ICoopPlayerInteractionHandler();
        this.hp = new Health ( this , Transform.I. translated (0 , 1.75f) , MAX_LIFE ,
                true );
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

        if (timer > 0) {
            timer--;
        }



    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        animation.draw(canvas);
        hp.draw(canvas);
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
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return (keyboard.get(keys.useItem()).isPressed());
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
        this.doorIsPassed = doorIsPassed;
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
    public Element element() {
        return this.element;
    }

    public void resetHealth() {
        hp.resetHealth();
    }

    public boolean invincible() {
        return !(timer == 0);
    }



    public void damage(DamageType damageType, int damage) {
        if (damageType != immunity && !invincible() && hp.isOn()) {
            timer = IFRAMES;
            hp.decrease(damage);
        }
    }

    public boolean isDead() {
        return hp.isOff();
    }


    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                setCurrentDoor(door);
                setDoorIsPassed(true);
            }
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            if (!isCellInteraction) {
              bomb.activate();
            }
            else {
                bomb.collect();
            }
        }

        /*@Override
        public void interactWith(ICoopCollectable other, boolean isCellInteraction) {
            if (other instanceof Bomb) {
                other.collect();
            }
            System.out.println("test3");




        }

         */










    }
}

