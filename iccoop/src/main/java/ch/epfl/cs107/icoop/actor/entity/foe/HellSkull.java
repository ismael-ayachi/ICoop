package ch.epfl.cs107.icoop.actor.entity.foe;

import ch.epfl.cs107.icoop.actor.entity.projectile.Flame;
import ch.epfl.cs107.icoop.actor.entity.player.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

/**
 * A stationary enemy that periodically spawns flames and damages players on interaction.
 */
public class HellSkull extends Foe implements Interactable {

    private final static int ANIMATION_DURATION = 15;

    private final static int MIN = 12;
    private final static int MAX = 48;
    private final Timer flameSpawnTimer;
    private final HellSkullInteractionHandler handler;

    Orientation[] orders = new Orientation []{ Orientation .UP ,
            Orientation . LEFT , Orientation . DOWN , Orientation . RIGHT };

    private final OrientedAnimation animation = new OrientedAnimation ("icoop/flameskull",
            ANIMATION_DURATION /3 , this ,
            new Vector(-0.5f , -0.5f) , orders ,
            3, 2, 2, 32 , 32 , true );

    /**
     * Creates a HellSkull.
     * @param area (Area): The area where the HellSkull is located, not null.
     * @param orientation (Orientation): The orientation of the HellSkull, not null.
     * @param position (DiscreteCoordinates): The initial position of the HellSkull, not null.
     */
    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position, 1, DamageType.PHYSICAL, DamageType.WATER);
        flameSpawnTimer = new Timer();
        handler = new HellSkullInteractionHandler();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isDead()) {
            animation.update(deltaTime);
            if (flameSpawnTimer.isOff()) {
                flameSpawnTimer.start(RandomGenerator.getInstance().nextInt(MIN , MAX));
                spawnFlame();
            }
        }
        flameSpawnTimer.tick();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isDead()) {
            animation.draw(canvas);
        }
    }

    /**
     * Spawns a flame projectile in the HellSkull's orientation.
     */
    public void spawnFlame() {
        Flame flame = new Flame(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        getOwnerArea().registerActor(flame);
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
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Interaction handler for the HellSkull.
     */
    private static class HellSkullInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.damage(DamageType.FIRE, 1);
        }
    }
}
