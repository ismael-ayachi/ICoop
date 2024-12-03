package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {

    private static final int ANIMATION_DURATION = 24;
    private final Animation explosionOff = new Animation ("icoop/explosive", 2, 1, 1, this , 16 , 16 ,
            ANIMATION_DURATION /2 , true );
    private final Animation explosionOn = new Animation ("icoop/explosion", 7, 1, 1, this , 32 , 32 ,
            ANIMATION_DURATION /7 , false );

    private boolean isExploding = false;
    private boolean exploded = false;

    private int bombTimer = 3*24 ;

    private BombInteractionHandler handler;

    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler = new BombInteractionHandler();
    }

    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position, int bombTimer) {
        super(area, orientation, position);
        this.bombTimer=bombTimer;
        handler = new BombInteractionHandler();
    }

    public void activate() {
        isExploding = true;
    }

    public void tickBombTimer() {
        this.bombTimer--;
    }



    public void explode() {
        if (isExploding && bombTimer==0) {
            exploded = true;
            isExploding = false;

        }
    }

    @Override
    public void update(float deltatime) {
        super.update(deltatime);

        if (exploded && bombTimer==0) {
            explosionOn.update(deltatime);

        }

        else if (isExploding) {
            explosionOff.update(deltatime);
            tickBombTimer();
        }
        explode();


    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        //explosionOn.setSpeedFactor(50);


        if (exploded && bombTimer==0 && !explosionOn.isCompleted()) {
            explosionOn.draw(canvas);
            explosionOn.update(5*24);
        }


        else if(!exploded && bombTimer!=0 && !explosionOff.isCompleted())  {
            explosionOff.draw(canvas);
            //explosionOff.setSpeedFactor(1);
        }


    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        if (exploded && isExploding) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        if (!exploded) {
            return true;
        }
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        if (exploded) {
            return true;
        }
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        handler.interactWith(other, isCellInteraction);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
        for(Orientation orientation : Orientation.values()) {
            fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(orientation.toVector()));
        }
        return fieldOfViewCells;

    }

    private class BombInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Interactable interactable, boolean isCellInteraction) {
            if (interactable instanceof Rock) {
                interactWith((Rock) interactable, isCellInteraction);
            }
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            rock.destroy();
        }
    }
}
