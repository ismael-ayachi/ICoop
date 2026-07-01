package ch.epfl.cs107.icoop.actor.decor;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a rock obstacle that can block movement and interactions until destroyed.
 * The rock is drawn on the canvas and can be interacted with or removed dynamically.
 */
public class Rock extends Obstacle {
    private boolean isDestroyed = false;
    private Sprite sprite = new Sprite("rock.1", 1f, 1f, this);

    /**
     * Creates a Rock entity at a specified position and orientation within an area.
     * @param area (Area): The area where the rock is located, not null
     * @param orientation (Orientation): The orientation of the rock, not null
     * @param position (DiscreteCoordinates): The position of the rock in the area, not null
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public boolean takeCellSpace() {
        return !isDestroyed;
    }

    @Override
    public boolean isCellInteractable() {
        return !isDestroyed;
    }

    @Override
    public boolean isViewInteractable() {
        return !isDestroyed;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            sprite.draw(canvas);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Destroys the rock, making it no longer occupy space or allow interactions.
     */
    public void destroy(){
        isDestroyed = true;
    }

}
