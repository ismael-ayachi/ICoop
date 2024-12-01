package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Rock extends Obstacle{
    private boolean isDestroyed = false;
    private Sprite sprite = new Sprite("rock.1", 1f, 1f, this);

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

    public void destroy(){
        isDestroyed = true;
    }

}
