package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a collectible heart that provides benefits when collected by a player.
 * Includes a simple animation while being displayed in the game area.
 */
public class Heart extends ICoopCollectable {
    private static final int ANIMATION_DURATION = 24;
    private final Animation heart = new Animation ("icoop/heart", 4, 1, 1, this , 16 , 16 ,
            ANIMATION_DURATION /4 , true);

    /**
     * Constructs a Heart collectible at a specific position in the given area.
     * @param area (Area): The area where the heart is located, not null.
     * @param orientation (Orientation): The orientation of the heart, not null.
     * @param position (DiscreteCoordinates): The position of the heart, not null.
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        heart.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        heart.update(deltaTime);
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

}