package ch.epfl.cs107.icoop.actor.entity.foe;

import ch.epfl.cs107.icoop.actor.entity.projectile.ElementalBall;
import ch.epfl.cs107.icoop.actor.elemental.DamageType;
import ch.epfl.cs107.icoop.handler.utilities.Timer;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;
import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.DOWN;

public class DarkLord extends Foe {

    /**
     * Represents the Dark Lord, a powerful enemy that can attack, summon allies, and become temporarily invisible.
     */
    private static final int MOVE_DURATION = 16;
    private static final int MIN_INVISIBILITY = 12;
    private static final int MAX_INVISIBILITY = 96;
    private static final int MAX_INACTIVITY = 24;

    private static final Vector anchor = new Vector(-0.5f, 0);
    private static final Orientation[] orders = {UP , LEFT , DOWN, RIGHT};
    private static final int ANIMATION_DURATION = 24;
    private final OrientedAnimation animation;
    private final OrientedAnimation spellAnimation;

    private DarkLordState currentState;
    private final Timer invisibilityTimer;
    private final Timer spellTimer;

    /**
     * Represents the different states of the Dark Lord.
     */
    public enum DarkLordState{
        IDLE("icoop/darkLord"), ATTACKING_WITH_STAFF("icoop/darkLord.spell");

        public final String name;

        DarkLordState(String name){
            this.name = name;
        }
    }

    /**
     * Creates a Dark Lord entity.
     * @param area       (Area): the area where the Dark Lord resides, not null.
     * @param orientation (Orientation): initial orientation, not null.
     * @param position   (DiscreteCoordinates): initial position, not null.
     */
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 50, DamageType.FIRE, DamageType.WATER, DamageType.PHYSICAL);
        this.currentState = DarkLordState.IDLE;
        this.invisibilityTimer = new Timer();
        this.spellTimer = new Timer();
        this.animation = new OrientedAnimation(DarkLordState.IDLE.name, ANIMATION_DURATION/4,
                this , anchor , orders , 3, 2, 2, 32, 32,
                true);
        this.spellAnimation = new OrientedAnimation(DarkLordState.ATTACKING_WITH_STAFF.name, ANIMATION_DURATION/4,
                this , anchor , orders , 3, 2, 2, 32, 32,
                true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        switch (currentState){
            case IDLE: {
                randomDisplacement();
                animation.update(deltaTime);
                break;
            }
            case ATTACKING_WITH_STAFF: {
                randomAttack();
                spellAnimation.update(deltaTime);
                break;
            }
        }
        spellTimer.tick();
        invisibilityTimer.tick();
    }

    @Override
    public void draw(Canvas canvas) {
        if(invisibilityTimer.isOff()){
            super.draw(canvas);
            switch (currentState){
                case IDLE: animation.draw(canvas); break;
                case ATTACKING_WITH_STAFF: spellAnimation.draw(canvas); break;
            }
        }
    }

    /**
     * Manages random movements of the Dark Lord, including orientation changes and state transitions.
     */
    public void randomDisplacement() {
        if (!isDisplacementOccurs()) {
            int randomOrientationIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
            double randomizeOrientation = RandomGenerator.getInstance().nextDouble();
            if (randomizeOrientation < 0.4) {
                orientate(fromInt(randomOrientationIndex));
            }
            move(MOVE_DURATION);

            double randomizeInactivity = RandomGenerator.getInstance().nextDouble();
            if (randomizeInactivity < 0.5) {
                currentState= DarkLordState.ATTACKING_WITH_STAFF;
            }
        }
    }

    /**
     * Executes random attacks, such as becoming invisible, summoning allies, or casting spells.
     */
    public void randomAttack(){
        if (spellTimer.isOff()) {
            double randomizeAttack = RandomGenerator.getInstance().nextDouble();
            if (randomizeAttack < 0.1) {
                invisibilityTimer.start(RandomGenerator.getInstance().nextInt(MIN_INVISIBILITY, MAX_INVISIBILITY));
            } else if (randomizeAttack > 0.8) {
                currentState = DarkLordState.ATTACKING_WITH_STAFF;
                spawnFireBalls();
            } else if (randomizeAttack>0.6 && randomizeAttack<0.8 ){
                summonFoe();
            }
        }
        if(RandomGenerator.getInstance().nextDouble()<0.1)
            currentState = DarkLordState.IDLE;
        startSpellTimer();
    }


    /**
     * Starts a random inactivity timer for the DarkLord.
     */
    public void startSpellTimer() {
        spellTimer.start(RandomGenerator.getInstance().nextInt(MAX_INACTIVITY));
    }
    
    /**
     * Spawns elemental fireballs in all four cardinal directions.
     */
    public void spawnFireBalls(){
        Orientation[] order = new Orientation[]{UP,DOWN,LEFT,RIGHT};
        for (Orientation orientation : order) {
            ElementalBall elementalBall = new ElementalBall(getOwnerArea(),orientation, getCurrentMainCellCoordinates().jump(orientation.toVector()),
                    ElementalBall.BallType.MAGIC_BALL);
            if (getOwnerArea().canEnterAreaCells(elementalBall, getCurrentMainCellCoordinates().getNeighbours())){
                getOwnerArea().registerActor(elementalBall);
            }
        }
    }

    /**
     * Summons random allies (HellSkull or BombFoe) to aid the Dark Lord in battle.
     */
    public void summonFoe(){
        double randomizeFoe = RandomGenerator.getInstance().nextDouble();
        if(randomizeFoe >0.9){
            HellSkull hellSkull = new HellSkull(getOwnerArea(), getOrientation(), getFieldOfViewCells().getFirst());
            if(getOwnerArea().canEnterAreaCells(hellSkull, getFieldOfViewCells()))
                getOwnerArea().registerActor(hellSkull);
        } else if (randomizeFoe <0.05){
            BombFoe bombFoe = new BombFoe(getOwnerArea(), getOrientation(), getFieldOfViewCells().getFirst());
            if(getOwnerArea().canEnterAreaCells(bombFoe, getFieldOfViewCells()))
                getOwnerArea().registerActor(bombFoe);
        }
    }

    @Override
    public boolean orientate(Orientation orientation){
        return super.orientate(orientation);
    }

}
