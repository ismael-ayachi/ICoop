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
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
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
    private final String prefix;
    private final static int ANIMATION_DURATION = 4;
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private OrientedAnimation animation;
    private KeyBindings.PlayerKeyBindings keys = KeyBindings.RED_PLAYER_KEY_BINDINGS;
    public ICoopPlayerInteractionHandler handler;
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
        this.prefix = prefix;
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
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            animation.reset();
        }



        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));
        super.update(deltaTime);

    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        animation.draw(canvas);


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
        if (keyboard.get(keys.useItem()).isPressed()) {
            return true;
        }
        return false;

    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        if (other instanceof Door) {
            handler.interactWith((Door)other, isCellInteraction);
        }
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
                move(ANIMATION_DURATION);
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
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    @Override
    public Element element() {
        return this.element;
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                setCurrentDoor(door);
                setDoorIsPassed(true);
                System.out.println("test");
            }
        }
    }
}

