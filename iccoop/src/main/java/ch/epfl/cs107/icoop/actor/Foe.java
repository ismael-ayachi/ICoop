package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Foe extends MovableAreaEntity implements Interactor {

    private static final int IFRAMES = 8;
    private final int MAX_LIFE;
    private final Health hp;

    private final DamageType[] weaknesses;
    private final Timer invincibilityTimer;

    private final static int ANIMATION_DURATION = 24;
    private final Animation deathAnimation = new Animation ("icoop/vanish", 7, 2, 2, this , 32 , 32 , new Vector ( -0.5f , 0f) , ANIMATION_DURATION /7 , false );

    public Foe(Area area, Orientation orientation, DiscreteCoordinates position, int maxLife, DamageType ... weaknesses) {
        super(area, orientation, position);
        this.MAX_LIFE = maxLife;
        this.hp = new Health(this, Transform.I.translated(0,1.75f),MAX_LIFE,false);
        this.weaknesses = weaknesses;
        this.invincibilityTimer = new Timer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isDead()){
            deathAnimation.update(deltaTime);
            if (deathAnimation.isCompleted())
                getOwnerArea().unregisterActor(this);
        }
        invincibilityTimer.tick();
    }

    @Override
    public void draw(Canvas canvas) {
        if(isDead()){
            deathAnimation.draw(canvas);
        }
        hp.draw(canvas);
    }

    public boolean isDead() {
        return hp.isOff();
    }

    public void damage(DamageType damageType, int quantity){
        for(DamageType weakness : weaknesses){
            if (weakness.equals(damageType) && invincibilityTimer.isOff()) {
                hp.decrease(quantity);
                invincibilityTimer.setTime(IFRAMES);
            }
        }
    }



    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

    }

    @Override
    public boolean takeCellSpace() {
        return !isDead();
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

}

