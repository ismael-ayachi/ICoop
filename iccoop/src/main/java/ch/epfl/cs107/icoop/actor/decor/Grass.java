package ch.epfl.cs107.icoop.actor.decor;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a patch of grass that can be interacted with.
 * Displays an animation upon being cut and is removed from the area once the animation completes.
 */
public class Grass extends AreaEntity {

    private final Sprite sprite = new Sprite("icoop/grass", 1f, 1f, this);
    private final Animation animation = new Animation ("icoop/grass.sliced", 4, 1, 1, this , 32 , 32 , 8 , false );
    private boolean wasCut;

    /**
     * Constructs a patch of grass at a specific position in the given area.
     * @param area (Area): The area where the grass is located, not null.
     * @param orientation (Orientation): The orientation of the grass, not null.
     * @param position (DiscreteCoordinates): The position of the grass, not null.
     */
    public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if (wasCut) {
            animation.update(deltaTime);
            if (animation.isCompleted()){
                getOwnerArea().unregisterActor(this);
            }
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        if(wasCut) {
            animation.draw(canvas);
        } else sprite.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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

    /**
     * Marks the grass as cut, initiating the cut animation.
     */
    public void cut(){
        wasCut = true;
    }
}
