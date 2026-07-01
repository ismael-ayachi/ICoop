package ch.epfl.cs107.icoop.actor.entity.projectile;

import ch.epfl.cs107.icoop.actor.entity.props.Bomb;
import ch.epfl.cs107.icoop.actor.decor.Obstacle;
import ch.epfl.cs107.icoop.actor.decor.Rock;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.actor.entity.Unstoppable;
import ch.epfl.cs107.icoop.actor.entity.foe.Foe;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents an elemental projectile with damage types, and interactions.
 * Can interact with various entities and applies specific damages based on its type and element.
 */
public class ElementalBall extends Projectile implements Unstoppable, ElementalEntity {

    private static final int ANIMATION_DURATION = 12;
    private final Animation ballAnimation;
    private final BallInteractionHandler handler;

    private final Element element;
    private final DamageType damageType;

    /**
     * Defines the types of elemental balls, each with a unique animation, element, and damage type.
     */
    public enum BallType {
        FIRE_BALL("icoop/magicFireProjectile", ElementalEntity.Element.FIRE, DamageType.FIRE),
        WATER_BALL("icoop/magicWaterProjectile", ElementalEntity.Element.WATER, DamageType.WATER),
        MAGIC_BALL("icoop/magicElementalProjectile", ElementalEntity.Element.MAGIC, DamageType.MAGIC);

        private final String name;
        private final Element element;
        private final DamageType damageType;

        /**
         * Creates a BallType with the associated properties.
         * @param name (String): Path to the animation file.
         * @param element (ElementalEntity.Element): The element of the ball.
         * @param damageType (DamageType): The damage type of the ball.
         */
        BallType(String name, ElementalEntity.Element element, DamageType damageType) {
            this.name = name;
            this.element = element;
            this.damageType = damageType;
        }
    }

    /**
     * Creates an ElementalBall with a specific type and properties.
     * @param area (Area): The area where the ball is located, not null.
     * @param orientation (Orientation): The direction of the ball, not null.
     * @param position (DiscreteCoordinates): The position of the ball, not null.
     * @param balltype (BallType): The type of the ball, defining its behavior and appearance.
     */
    public ElementalBall(Area area, Orientation orientation, DiscreteCoordinates position, BallType balltype) {
        super(area, orientation, position, 2, 10);
        this.ballAnimation = new Animation (balltype.name, 4, 1, 1, this , 32 , 32 ,
                ANIMATION_DURATION /4 , true);
        handler = new BallInteractionHandler();
        this.element = balltype.element;
        this.damageType = balltype.damageType;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        ballAnimation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        ballAnimation.draw(canvas);
    }

    @Override
    public void interactWith(Interactable other,boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public Element element() {
        return element;
    }

    /**
     * Custom interaction handler for the ball's interactions.
     */
    private class BallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            foe.damage(damageType, 1);
            stopProjectile();
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            bomb.explode();
            stopProjectile();
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction){
            rock.destroy();
        }

        @Override
        public void interactWith(Obstacle obstacle, boolean isCellInteraction) {
            stopProjectile();
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (element == Element.MAGIC) {
                player.damage(damageType, 1);
            }
        }
    }
}
