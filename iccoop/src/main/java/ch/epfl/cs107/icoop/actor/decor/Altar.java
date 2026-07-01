package ch.epfl.cs107.icoop.actor.decor;

import ch.epfl.cs107.icoop.actor.collectables.Heart;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a static, non-interactable dead tree occupying multiple cells in an area.
 * Handles its own animation and rendering.
 */
public class Altar extends AreaEntity {
    private final Timer spawnTimer = new Timer();

    private final Animation animation = new Animation ("icoop/altar", 5, 1, 2, this,
            16 , 32 , new Vector(0,0), 4 , true);

    /**
     * Creates a DeadTree at a specific position and orientation in the given area.
     * @param area (Area): the area where the tree is located, not null.
     * @param orientation (Orientation): the orientation of the tree, not null.
     * @param position (DiscreteCoordinates): the main cell position of the tree, not null.
     */
    public Altar(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if(spawnTimer.isOff()){
            spawnTimer.start(720);
            spawnHeart();
        } else spawnTimer.tick();

        if (spawnTimer.isGoing()){
            animation.update(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v, boolean isCellInteraction ) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

    /**
     * Spawns a Heart near the Altar.
     * The Heart is placed in the cell adjacent to the Altar in its orientation direction.
     */
    public void spawnHeart(){
        Heart heart = new Heart(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        getOwnerArea().registerActor(heart);
    }
}
