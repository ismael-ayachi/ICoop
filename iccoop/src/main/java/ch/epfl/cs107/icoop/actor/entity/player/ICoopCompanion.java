package ch.epfl.cs107.icoop.actor.entity.player;

import ch.epfl.cs107.icoop.actor.entity.projectile.ElementalBall;
import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.actor.entity.foe.Foe;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.entity.TargetEntity;
import ch.epfl.cs107.icoop.handler.entity.TargetFollower;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static ch.epfl.cs107.play.math.Orientation.*;


/**
 * Represents a companion assisting the player by healing, attacking enemies, and evolving to gain new abilities.
 * The companion follows the player and can switch between idle and attacking states.
 */
public class ICoopCompanion extends MovableAreaEntity implements TargetFollower, Interactor, ElementalEntity {

    private static final int MOVE_DURATION = 24;

    private static final Vector anchor = new Vector(-.45f, 0);
    private static final Orientation[] orders = {DOWN, LEFT, RIGHT, UP};
    private static final int ANIMATION_DURATION = 12;
    private final OrientedAnimation animation;
    private final OrientedAnimation evolvedAnimation;

    private final TargetEntity player;
    private TargetEntity enemyTarget;

    private boolean evolved;
    private final Element element;
    private final CompanionType companionType;

    private final Timer passiveTimer;
    private final Timer healTimer;

    private final CompanionInteractionHandler handler;
    private ICoopCompanionState currentState;

    /**
     * Represents the companion's states: idle (following player) or attacking.
     */
    public enum ICoopCompanionState {

        IDLE(3, 8),
        ATTACKING( 3, 3);

        public final int speedFactor;
        public final int viewDistance;

        ICoopCompanionState(int speedFactor, int viewDistance) {
            this.speedFactor = speedFactor;
            this.viewDistance = viewDistance;
        }
    }

    /**
     * Defines the types of companions with unique attributes and animations.
     */
    public enum CompanionType {
        RED_COMPANION("icoop/companion_fire", "icoop/companion_fire.evolved", Element.FIRE, ElementalBall.BallType.FIRE_BALL),
        BLUE_COMPANION("icoop/companion_water", "icoop/companion_water.evolved", Element.WATER, ElementalBall.BallType.WATER_BALL);

        public final String prefix;
        public final String evolvedPrefix;
        public final Element element;
        public final ElementalBall.BallType ballType;


        /**
         * Creates a CompanionType with associated properties.
         *
         * @param prefix (String) Path prefix for the companion's sprite
         * @param evolvedPrefix (String) Path prefix for the evolved companion's sprite
         * @param element (Element) Element associated with the companion
         * @param ballType (ElementalBall) Type of elemental projectile the companion uses
         */
        CompanionType(String prefix, String evolvedPrefix, Element element, ElementalBall.BallType ballType) {
            this.prefix = prefix;
            this.evolvedPrefix = evolvedPrefix;
            this.element = element;
            this.ballType = ballType;
        }
    }
    

    /**
     * Creates an ICoopCompanion entity.
     * @param area (Area): the area where the companion is located, not null
     * @param orientation (Orientation): the initial orientation, not null
     * @param position (DiscreteCoordinates): the starting position, not null
     * @param companionType (CompanionType): the type of the companion
     * @param player (TargetEntity): the player the companion follows
     */
    public ICoopCompanion(Area area, Orientation orientation, DiscreteCoordinates position, CompanionType companionType, TargetEntity player) {
        super(area, orientation, position);
        this.currentState = ICoopCompanionState.IDLE;
        this.handler = new CompanionInteractionHandler();
        this.passiveTimer = new Timer();
        this.healTimer = new Timer();
        this.element = companionType.element;
        this.player = player;
        this.companionType = companionType;

        this.animation = new OrientedAnimation(companionType.prefix, ANIMATION_DURATION/4, this,
                anchor, orders, 4, 2, 2, 32, 32, true);
        this.evolvedAnimation = new OrientedAnimation(companionType.evolvedPrefix, ANIMATION_DURATION/3, this,
                anchor, orders, 4, 2, 2, 32, 32, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isDisplacementOccurs()) {
            switch (currentState) {
                case IDLE: {
                    targetDisplacement(player);
                    move(MOVE_DURATION / currentState.speedFactor);
                }
                case ATTACKING: {
                    if (enemyTarget != null) {
                        targetDisplacement(enemyTarget);
                        move(MOVE_DURATION / currentState.speedFactor);
                    }
                }
            }
        } else {
            if (evolved)
                evolvedAnimation.update(deltaTime);
            else
                animation.update(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(evolved) {
            evolvedAnimation.draw(canvas);
        } else {
            animation.draw(canvas);
        }
    }

    @Override
    public void setTarget(TargetEntity target) {
        this.enemyTarget = target;
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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Registers the companion in a new area and sets its position.
     * @param area (Area) Area where the companion is added
     * @param position (DiscreteCoordinates) New position of the companion
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Unregisters the companion from its current area.
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public boolean takeCellSpace(){
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return passiveTimer.isOff();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean isCellInteractable(){
        return false;
    }

    @Override
    public boolean isViewInteractable(){
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public Element element(){
        return element;
    }

    public void spawnElementalBall(){
        ElementalBall ball = new ElementalBall(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), companionType.ballType);
        getOwnerArea().registerActor(ball);
    }

    /**
     * Setting the evolution of the companion to true, enabling advanced abilities and updating its appearance.
     */
    public void evolve(){
        this.evolved=true;
    }


    /**
     * Manages interactions between the companion and other entities.
     */
    private class CompanionInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (!evolved && healTimer.isOff()) {
                player.heal(1);
                healTimer.start(480);
            }
        }

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (evolved) {
                switch(currentState) {
                    case IDLE: {
                        setTarget(foe);
                        currentState = ICoopCompanionState.ATTACKING;
                    }
                    case ATTACKING: {
                        spawnElementalBall();
                        currentState = ICoopCompanionState.IDLE;
                        passiveTimer.start(240);
                    }
                }
            }
        }
    }
}




