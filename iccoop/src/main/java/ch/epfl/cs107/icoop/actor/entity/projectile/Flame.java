package ch.epfl.cs107.icoop.actor.entity.projectile;

import ch.epfl.cs107.icoop.actor.entity.props.Bomb;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.entity.Unstoppable;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a flame projectile that interacts with entities and causes fire damage.
 */
public class Flame extends Projectile implements Unstoppable {

    private final Animation animation = new Animation("icoop/fire", 7, 1, 1,
            this , 16 , 16 , 4, true );
    private final FlameInteractionHandler handler;




    /**
     * Creates a Flame projectile.
     * @param area (Area): The area where the flame is located, not null.
     * @param orientation (Orientation): The direction of the flame, not null.
     * @param position (DiscreteCoordinates): The starting position of the flame, not null.
     */
    public Flame(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 2, 8);
        this.handler = new FlameInteractionHandler();

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

    /**
     * Interaction handler for the flame.
     */
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
