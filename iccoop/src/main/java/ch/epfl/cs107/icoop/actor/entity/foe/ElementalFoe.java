package ch.epfl.cs107.icoop.actor.entity.foe;

import ch.epfl.cs107.icoop.actor.entity.projectile.ElementalBall;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.entity.TargetEntity;
import ch.epfl.cs107.icoop.handler.entity.TargetFollower;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;
import java.util.ArrayList;
import java.util.List;
import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * Represents an elemental foe capable of following a target and casting magic attacks.
 * Operates in two states: IDLE (wandering) and ATTACKING (chasing a target and attacking).
 * Manages animations, interactions, and its elemental magic abilities.
 */
public class ElementalFoe extends Foe implements TargetFollower, ElementalEntity {

    private static final int MOVE_DURATION = 24;
    private static final int MAX_INACTIVITY = 48;

    private final Vector anchor = new Vector(-.45f, 0);
    private final Orientation[] orders = {DOWN, LEFT, RIGHT, UP};
    private final int ANIMATION_DURATION = 12;
    private final OrientedAnimation animation = new OrientedAnimation("icoop/elementalFoe", ANIMATION_DURATION/4, this,
                    anchor, orders, 4, 2, 2, 32, 32, true);

    private TargetEntity target;

    private final Timer inactivityTimer;

    private final ElementalFoeInteractionHandler handler;
    private ElementalFoeState currentState;
    
    /**
     * Defines the states of the ElementalFoe.
     */
    public enum ElementalFoeState {

        IDLE(2, 5),
        ATTACKING( 3, 2);

        public final int speedFactor;
        public final int viewDistance;

        /**
         * Creates a state with associated properties.
         * @param speedFactor (int): Speed multiplier for movement.
         * @param viewDistance (int): Distance of field of view.
         */
        ElementalFoeState(int speedFactor, int viewDistance) {
            this.speedFactor = speedFactor;
            this.viewDistance = viewDistance;
        }
    }

    /**
     * Creates an ElementalFoe in a specific area and position.
     * @param area (Area): The area where the foe is located, not null.
     * @param orientation (Orientation): The initial orientation of the foe, not null.
     * @param position (DiscreteCoordinates): The position of the foe, not null.
     */
    public ElementalFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 16, DamageType.PHYSICAL);
        this.currentState = ElementalFoeState.IDLE;
        this.handler = new ElementalFoeInteractionHandler();
        this.inactivityTimer = new Timer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inactivityTimer.isOff()) {
            switch (currentState) {
                case IDLE: {
                    randomDisplacement();
                    break;
                }
                case ATTACKING: {
                    if (target!=null && !isDisplacementOccurs()) {
                        targetDisplacement(target);
                        move(MOVE_DURATION / currentState.speedFactor);
                    }
                    break;
                }
            }
        } else inactivityTimer.tick();

        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isDead()) {
            animation.draw(canvas);
        }
    }

    /**
     * Starts a random inactivity timer.
     */
    public void startInactivity() {
        inactivityTimer.start(RandomGenerator.getInstance().nextInt(MAX_INACTIVITY));
    }


    /**
     * Handles random movement in the IDLE state.
     */
    public void randomDisplacement() {
        if (!isDisplacementOccurs()) {
            int randomOrientationIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
            double randomizeOrientation = RandomGenerator.getInstance().nextDouble();
            if (randomizeOrientation < 0.6) {
                orientate(fromInt(randomOrientationIndex));
            }
            move(MOVE_DURATION / currentState.speedFactor);
            double randomizeInactivity = RandomGenerator.getInstance().nextDouble();
            if (randomizeInactivity < 0.2) {
                startInactivity();
            }
        }
    }

    /**
     * Spawns a magic ball as part of an attack.
     */
    public void spawnMagicBall() {
        ElementalBall ball = new ElementalBall(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), ElementalBall.BallType.MAGIC_BALL);
        getOwnerArea().registerActor(ball);
    }

    @Override
    public void setTarget(TargetEntity target) {
        this.target = target;
    }

    @Override
    public boolean orientate(Orientation orientation){
        return super.orientate(orientation);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List <DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
        for (int i = 1; i <= currentState.viewDistance; i++) {
            fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));
        }
        return fieldOfViewCells;
    }

    @Override
    public boolean wantsViewInteraction() {
        return inactivityTimer.isOff();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public Element element(){
        return Element.MAGIC;
    }

    /**
     * Handles interactions specific to the ElementalFoe.
     */
    private class ElementalFoeInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (inactivityTimer.isOff()){
                switch (currentState) {
                    case IDLE: {
                        currentState = ElementalFoeState.ATTACKING;
                        setTarget(player);
                    }
                    case ATTACKING: {
                         spawnMagicBall();
                         currentState = ElementalFoeState.IDLE;
                         startInactivity();
                    }
                }
            }
        }
    }

}
