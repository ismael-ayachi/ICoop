package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.actor.collectables.ICoopCollectable;
import ch.epfl.cs107.icoop.actor.decor.Grass;
import ch.epfl.cs107.icoop.actor.decor.Rock;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.actor.entity.foe.ElementalFoe;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;


/**
 * Bomb which can be activated and explode after an amount of time, interacting with its environment.
 * A bomb can damage entities or destroy obstacles.
 */
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

    private final Timer bombTimer;

    private final BombInteractionHandler handler;

    /**
     * Creates a bomb with a default timer.
     * @param area (Area): the area where the bomb is located, not null
     * @param orientation (Orientation): the orientation of the bomb, not null
     * @param position (DiscreteCoordinates): the position of the bomb, not null
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler = new BombInteractionHandler();
        bombTimer = new Timer();
    }
    
    /**
     * Creates a bomb with a custom timer.
     * @param area (Area): the area where the bomb is located, not null
     * @param orientation (Orientation): the orientation of the bomb, not null
     * @param position (DiscreteCoordinates): the position of the bomb, not null
     * @param bombTimer (int): the timer duration for the bomb, in ticks
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position, int bombTimer) {
        super(area, orientation, position);
        this.bombTimer = new Timer(bombTimer);
        handler = new BombInteractionHandler();
        activate();
    }


    /**
     * Activates the bomb, starting the countdown for its explosion.
     */
    
    public void activate() {
        isExploding = true;
        bombTimer.start(ANIMATION_DURATION*3);
    }

    /**
     * Collects the bomb if it has not been activated or exploded.
     */
    
    public void collect() {
        if (!(isExploding||exploded)) {
            super.collect();
        }
    }

    /**
     * Handles the bomb explosion logic, resetting its timer.
     */
    
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

    /**
     * Private interaction handler class for bomb-specific interactions.
     */
    
    private class BombInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            rock.destroy();
        }

        @Override
        public void interactWith(Grass grass, boolean isCellInteraction){
            grass.cut();
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

        @Override
        public void interactWith(ElementalFoe elementalFoe, boolean isCellInteraction) {
            elementalFoe.damage(DAMAGE_TYPE,DAMAGE_QUANTITY*8);
        }
    }
}
