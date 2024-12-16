package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.TargetEntity;
import ch.epfl.cs107.icoop.handler.TargetFollower;
import ch.epfl.cs107.icoop.handler.Timer;
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

    public enum BombFoeState {

        IDLE(2, 8, true, true),
        ATTACKING( 3, 1, true, true),
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

    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 2, DamageType.FIRE, DamageType.PHYSICAL);
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

    public void startInactivity() {
        inactivityTimer.setTime(RandomGenerator.getInstance().nextInt(MAX_INACTIVITY));
    }

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

    public void placeBomb() {
        Bomb bomb = new Bomb(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst(), Bomb.DEFAULT_BOMB_TIMER);
        if (getOwnerArea().canEnterAreaCells(bomb, getFieldOfViewCells())) {
            getOwnerArea().registerActor(bomb);
            currentState = BombFoeState.GUARDING;
            guard();
        }
    }

    public void guard() {
        protectionTimer.setTime(RandomGenerator.getInstance().nextInt(48,120));
    }


    public void setTarget(ICoopPlayer player) {
        target = player;
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

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (inactivityTimer.isOff()) {
                switch (currentState) {
                    case IDLE: {
                        currentState = BombFoeState.ATTACKING;
                        setTarget(player);
                        break;
                    }
                    case ATTACKING: {
                        placeBomb();
                    }
                }

            }
        }
    }

}
