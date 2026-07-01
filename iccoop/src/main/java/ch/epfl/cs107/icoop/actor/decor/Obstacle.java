package ch.epfl.cs107.icoop.actor.decor;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a stationary obstacle in the game that blocks movement and interacts with other entities.
 * Obstacles can have different visual appearances defined by their sprites.
 */
public class Obstacle extends AreaEntity {
    private Sprite sprite = new Sprite("rock.2", 1f, 1f, this);

    /**
     * Creates an Obstacle with a default sprite.
     * @param area (Area): The area where the obstacle is located
     * @param orientation (Orientation): The orientation of the obstacle
     * @param position (DiscreteCoordinates): The position of the obstacle
     */
    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * Creates an Obstacle with a specified sprite.
     * @param area (Area): The area where the obstacle is located
     * @param orientation (Orientation): The orientation of the obstacle
     * @param position (DiscreteCoordinates): The position of the obstacle
     * @param spriteName (String): The name of the sprite to use
     */
    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        this.sprite = new Sprite(spriteName, 1f, 1f, this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
