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
    private final int ANIMATION_DURATION = 24;
    private final OrientedAnimation unprotectedAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION/3,
            this , anchor , orders , 4, 2, 2, 32, 32,
            true);
    private final OrientedAnimation protectedAnimation = new OrientedAnimation("icoop/bombFoe.protecting",ANIMATION_DURATION/3,
            this , anchor , orders , 4, 2, 2, 32, 32,
            false);

    private final BombFoeInteractionHandler handler;
    private TargetEntity target;

    private BombFoeState state;
    private final Timer inactivityTimer;
    private final Timer protectionTimer;

    public enum BombFoeState {

        IDLE(2, 8, true, true),
        ATTACKING( 6, 2, true, true),
        GUARDING( 1, 1, false, false);

        public final int speedFactor;
        public final boolean aggressive;
        public final boolean damageable;
        public final int viewDistance;

        BombFoeState(int speedFactor, int viewDistance, boolean wantsInteraction, boolean willWalk) {
            this.speedFactor = speedFactor;
            this.viewDistance = viewDistance;
            this.aggressive = wantsInteraction;
            this.damageable = willWalk;
        }

    }

    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 2, DamageType.FIRE, DamageType.PHYSICAL);
        this.state = BombFoeState.IDLE;
        this.handler = new BombFoeInteractionHandler();
        this.inactivityTimer = new Timer();
        this.protectionTimer = new Timer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inactivityTimer.isOff()) {
            switch (state) {
                case IDLE: {
                    randomDisplacement();
                    unprotectedAnimation.update(deltaTime);
                    startInactivity();
                }
                case ATTACKING: {
                    if (target!=null &&targetDisplacement(target)) {
                        unprotectedAnimation.update(deltaTime);
                        move(MOVE_DURATION / state.speedFactor);
                        if (DiscreteCoordinates.distanceBetween(this.getCurrentMainCellCoordinates(), target.getCurrentMainCellCoordinates()) < 3) {
                            placeBomb();

                        }
                    }
                }
                case GUARDING: {
                    protectedAnimation.update(deltaTime);
                    guard();
                }
            }
        } else inactivityTimer.tick();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (state == BombFoeState.GUARDING) {
            protectedAnimation.draw(canvas);
        } else {
            unprotectedAnimation.draw(canvas);
        }
    }

    public void startInactivity() {
        if (inactivityTimer.isOff()){
            double randDouble = RandomGenerator.getInstance().nextDouble();
            if (randDouble < 0.3) {
                inactivityTimer.setTime(RandomGenerator.getInstance().nextInt(MAX_INACTIVITY));
            }
        }
    }

    public void randomDisplacement() {
        if (!isDisplacementOccurs()) {
            int randomInt = RandomGenerator.getInstance().nextInt(Orientation.values().length);
            double randDouble = RandomGenerator.getInstance().nextDouble();
            if (randDouble < 0.4) {
                orientate(fromInt(randomInt));
            }
            move(MOVE_DURATION / state.speedFactor);
        }
    }
/*
    public void targetDisplacement(ICoopPlayer player) {
        //float distance = DiscreteCoordinates.distanceBetween(this.getCurrentMainCellCoordinates(), player.getCurrentMainCellCoordinates());
        float BombFoeX = this.getCurrentMainCellCoordinates().toVector().getX();
        float BombFoeY = this.getCurrentMainCellCoordinates().toVector().getY();
        float TargetX = player.getCurrentMainCellCoordinates().toVector().getX();
        float TargetY = player.getCurrentMainCellCoordinates().toVector().getY();
        Vector BombFoeVector = new Vector(BombFoeX, BombFoeY);
        Vector TargetVector = new Vector(TargetX, TargetY);
        Vector v = TargetVector.sub(BombFoeVector);
        float deltaX = v.getX();
        float deltaY = v.getY();
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            orientate(fromVector(new Vector(deltaX,0)));
        }
        else if (Math.abs(deltaY) > Math.abs(deltaX)) {
            orientate(fromVector(new Vector(0, deltaY)));
        }
        else {
            move(MOVE_DURATION/state.speedFactor);
        }
    }

 */

    public void placeBomb() {
        Bomb bomb = new Bomb(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst(), Bomb.DEFAULT_BOMB_TIMER);
        if (getOwnerArea().canEnterAreaCells(bomb, getFieldOfViewCells())) {
            getOwnerArea().registerActor(bomb);
            state = BombFoeState.GUARDING;
        }
    }

    public void guard() {
        if(protectionTimer.isOff()){
            protectionTimer.setTime(RandomGenerator.getInstance().nextInt(48,96));
        } else state = BombFoeState.IDLE;
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
        for (int i = 1; i <= state.viewDistance; i++) {
            fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));
        }
        return fieldOfViewCells;
    }

    @Override
    public boolean wantsViewInteraction() {
        return state.aggressive;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return state.damageable;
    }

    @Override
    public boolean isViewInteractable() {
        return state.damageable;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (inactivityTimer.isOff()) {
                state = BombFoeState.ATTACKING;
                setTarget(player);
                //targetDisplacement(player);
                switch (state) {
                    case ATTACKING: {
                    }
                    case IDLE: {
                        state = BombFoeState.ATTACKING;
                        //targetDisplacement(player);
                    }
                }
            }
        }


        /*@Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            bomb.activate();
        }*/
    }

}
