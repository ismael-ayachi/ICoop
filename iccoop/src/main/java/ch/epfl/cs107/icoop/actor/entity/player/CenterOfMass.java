package ch.epfl.cs107.icoop.actor.entity.player;

import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;

/**
 * Represents the center of mass of a group of actors.
 * Calculates the average position and velocity of the given actors.
 */
public class CenterOfMass implements Actor {

    private final Actor[] actors;


    /**
     * Creates a CenterOfMass object for a group of actors.
     * @param actor (Actor): the first actor, not null
     * @param restOfActors (Actor...): the remaining actors, not null
     */
    public CenterOfMass(Actor actor, Actor... restOfActors) {
        this.actors = new Actor[restOfActors.length + 1];
        actors[0] = actor;
        System.arraycopy(restOfActors, 0, this.actors, 1, restOfActors.length);
    }

    @Override
    public Vector getPosition() {
        Vector position = Vector.ZERO;
        for (Actor actor : actors) {
            position = position.add(actor.getPosition());
        }
        return position.mul(1f / actors.length);
    }

    @Override
    public Transform getTransform() {
        return Transform.I.translated(getPosition());
    }

    @Override
    public Vector getVelocity() {
        Vector velocity = Vector.ZERO;
        for (Actor actor : actors) {
            velocity = velocity.add(actor.getVelocity());
        }
        return velocity.mul(1f / actors.length);
    }
}
