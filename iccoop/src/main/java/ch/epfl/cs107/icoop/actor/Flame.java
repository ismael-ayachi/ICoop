package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Flame extends Projectile implements Unstoppable {

    private final Animation animation = new Animation("icoop/fire", 7, 1, 1,
            this , 16 , 16 , 4, true );
    private final FlameInteractionHandler handler;

    public Flame(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int distance) {
        super(area, orientation, position, speed, distance);
        handler = new FlameInteractionHandler();


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);

    }

    @Override
    public void draw(Canvas canvas) {

        animation.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other,boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private static class FlameInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.damage(DamageType.FIRE, 1);
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction) {
            bomb.explode();
        }
    }
}
