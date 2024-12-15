package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.Timer;
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

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bomb extends ICoopCollectable implements Interactor {

    private static final int ANIMATION_DURATION = 24;
    private final Animation explosionOff = new Animation ("icoop/explosive", 2, 1, 1, this , 16 , 16 ,
            ANIMATION_DURATION /2 , true );
    private final Animation explosionOn = new Animation ("icoop/explosion", 7, 1, 1, this , 32 , 32 ,
            ANIMATION_DURATION /7 , false );

    private static final DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private static final int DAMAGE_QUANTITY = 2;

    public static final int DEFAULT_BOMB_TIMER = 72;

    private boolean isExploding = false;
    private boolean exploded = false;

    private Timer bombTimer;

    private final BombInteractionHandler handler;

    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler = new BombInteractionHandler();
        bombTimer = new Timer();
    }

    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position, int bombTimer) {
        super(area, orientation, position);
        this.bombTimer = new Timer(bombTimer);
        handler = new BombInteractionHandler();
        activate();
    }

    public void activate() {
        isExploding = true;
        bombTimer.setTime(ANIMATION_DURATION*3);
    }

    public void collect() {
        if (!(isExploding||exploded)) {
            super.collect();
        }
    }

    public void explode() {
        exploded = true;
        isExploding = false;
        bombTimer.reset();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (exploded) {
            explosionOn.update(deltaTime);
            if (explosionOn.isCompleted()) {
                getOwnerArea().unregisterActor(this);
            }
        }
        else if (isExploding) {
            explosionOff.update(deltaTime);
            bombTimer.tick();
            if (bombTimer.isOff()) explode();
        }
    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);

        if (exploded  && !explosionOn.isCompleted()) {
            explosionOn.draw(canvas);
        }
        else if(!exploded && !explosionOff.isCompleted())  {
            explosionOff.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    @Override
    public boolean isCellInteractable() {
        return !(exploded || isExploding);
    }

    @Override
    public boolean isViewInteractable() {
        return !(exploded||isExploding);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean wantsCellInteraction() {
        return exploded;
    }

    @Override
    public boolean wantsViewInteraction() {
        return exploded;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    private class BombInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            rock.destroy();
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.damage(DAMAGE_TYPE,DAMAGE_QUANTITY);
        }

        @Override
        public void interactWith(ElementalWall wall, boolean isCellInteraction) {
            wall.destroy();
        }

        @Override
        public void interactWith(Bomb bomb, boolean isCellInteraction){
            if(!isCellInteraction){
                bomb.explode();
            }
        }
    }
}
