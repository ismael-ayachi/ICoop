package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class ElementalBall extends Projectile implements Unstoppable, ElementalEntity {

    private static final int ANIMATION_DURATION = 12;
    private final Animation ballAnimation;
    private final BallInteractionHandler handler;

    private final Element element;
    private final DamageType damageType;

    public enum BallType {
        FIRE_BALL("icoop/magicFireProjectile", ElementalEntity.Element.FIRE, DamageType.FIRE),
        WATER_BALL("icoop/magicWaterProjectile", ElementalEntity.Element.WATER, DamageType.WATER);

        private final String name;
        private final Element element;
        private final DamageType damageType;

        BallType(String name, ElementalEntity.Element element, DamageType damageType) {
            this.name = name;
            this.element = element;
            this.damageType = damageType;
        }
    }

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

    private class BallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            foe.damage(damageType, 1);
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            bomb.explode();
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction){
            rock.destroy();
        }

        @Override
        public void interactWith(Obstacle obstacle, boolean isCellInteraction) {
            stopProjectile();
        }
    }
}
