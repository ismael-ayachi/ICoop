package ch.epfl.cs107.icoop.actor.entity.foe;

import ch.epfl.cs107.icoop.actor.entity.props.Bomb;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
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
 * Represents a specialized foe capable of targeting players, placing bombs, and switching between states.
 * Operates in three states: IDLE (random movement), ATTACKING (chasing a target), and GUARDING (defensive mode).
 * Features animations, interaction logic, and state-dependent behaviors.
 */

public class BombFoe extends Foe implements TargetFollower {

    private static final int MOVE_DURATION = 24;
    private static final int MAX_INACTIVITY = 48;

    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};
    private final int ANIMATION_DURATION = 12;
    private final OrientedAnimation unprotectedAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION/3,
            this , anchor , orders , 4, 2, 2, 32, 32,
            true);
    private final OrientedAnimation protectedAnimation = new OrientedAnimation("icoop/bombFoe.protecting",ANIMATION_DURATION/3,
            this , anchor , orders , 4, 2, 2, 32, 32,
            false);

    private final BombFoeInteractionHandler handler;
    private TargetEntity target;

    private BombFoeState currentState;
    private final Timer inactivityTimer;
    private final Timer protectionTimer;

    /**
     * Defines the different states of the BombFoe.
     */
    public enum BombFoeState {
        /**
         * IDLE: The BombFoe wanders randomly, can be damaged, and may detect players.
         */
        IDLE(2, 8, true, true),
        /**
         * ATTACKING: The BombFoe aggressively chases a target, attempting to place a bomb near them.
         */
        ATTACKING( 3, 1, true, true),
        /**
         * GUARDING: The BombFoe enters a defensive mode after placing a bomb, becoming invulnerable.
         */
        GUARDING( 1, 1, false, false);

        public final int speedFactor;
        public final boolean aggressive;
        public final boolean damageable;
        public final int viewDistance;

        BombFoeState(int speedFactor, int viewDistance, boolean wantsInteraction, boolean damageable) {
            this.speedFactor = speedFactor;
            this.viewDistance = viewDistance;
            this.aggressive = wantsInteraction;
            this.damageable = damageable;
        }
    }

    /**
     * Creates a BombFoe with default state and timers.
     * @param area (Area): the area where the BombFoe is located, not null
     * @param orientation (Orientation): the initial orientation of the BombFoe, not null
     * @param position (DiscreteCoordinates): the initial position of the BombFoe, not null
     */
    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 4, DamageType.FIRE, DamageType.PHYSICAL);
        this.currentState = BombFoeState.IDLE;
        this.handler = new BombFoeInteractionHandler();
        this.inactivityTimer = new Timer();
        this.protectionTimer = new Timer();
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
                        if (DiscreteCoordinates.distanceBetween(this.getCurrentMainCellCoordinates(), target.getCurrentMainCellCoordinates()) < 2) {
                            placeBomb();
                        }
                    }
                    break;
                }
                case GUARDING: {
                    protectionTimer.tick();
                    if(protectionTimer.isOff()){
                        currentState = BombFoeState.IDLE;
                        startInactivity();
                    }
                    break;
                }
            }
        } else inactivityTimer.tick();

        if (isDisplacementOccurs()) {
            unprotectedAnimation.update(deltaTime);
        } else if (currentState == BombFoeState.GUARDING){
            protectedAnimation.update(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isDead()) {
            if (currentState == BombFoeState.GUARDING) {
                protectedAnimation.draw(canvas);
            } else {
                unprotectedAnimation.draw(canvas);
            }
        }
    }


    /**
     * Starts a random inactivity timer for the BombFoe.
     */
    public void startInactivity() {
        inactivityTimer.start(RandomGenerator.getInstance().nextInt(MAX_INACTIVITY));
    }

    /**
     * Handles random movement while in IDLE state, with a chance to reorient or pause.
     */
    public void randomDisplacement() {
        if (!isDisplacementOccurs()) {
            int randomOrientationIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
            double randomizeOrientation = RandomGenerator.getInstance().nextDouble();
            if (randomizeOrientation < 0.4) {
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
     * Places a bomb at the BombFoe's field of view and switches to GUARDING state.
     */
    public void placeBomb() {
        Bomb bomb = new Bomb(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst(), Bomb.DEFAULT_BOMB_TIMER);
        if (getOwnerArea().canEnterAreaCells(bomb, getFieldOfViewCells())) {
            getOwnerArea().registerActor(bomb);
            currentState = BombFoeState.GUARDING;
            guard();
        }
    }

    /**
     * Initiates the guarding phase with a random duration.
     */
    public void guard() {
        protectionTimer.start(RandomGenerator.getInstance().nextInt(48,120));
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
        return currentState.aggressive && inactivityTimer.isOff();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return currentState.damageable;
    }

    @Override
    public boolean isViewInteractable() {
        return currentState.damageable;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }
    
    /**
     * Custom interaction handler for BombFoe-specific logic.
     */
    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (inactivityTimer.isOff() && currentState == BombFoeState.IDLE) {
                currentState = BombFoeState.ATTACKING;
                setTarget(player);
            }
        }
    }

}
