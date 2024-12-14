package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

public class Orb extends ElementalItem {

    private final static int ANIMATION_DURATION = 24;
    private final static int ANIMATION_FRAMES = 6;
    private final int spriteYDelta;
    final Sprite[] sprites = new Sprite [ ANIMATION_FRAMES ];
    private final Animation orbAnimation;

    private final Element element;
    private final DamageType damageType;

    private final Dialog orbDialog;
    private final DialogHandler dialogHandler;

    public enum OrbType {
        FIRE_ORB(64, Element.FIRE, DamageType.FIRE, new Dialog("orb_fire_msg")),
        WATER_ORB(0,Element.WATER, DamageType.WATER, new Dialog("orb_water_msg"));

        public final int spriteYDelta;
        public final Element element;
        public final DamageType damageType;
        public final Dialog orbDialog;


        OrbType(int spriteYDelta, Element element, DamageType damageType, Dialog orbDialog) {
            this.spriteYDelta = spriteYDelta;
            this.element = element;
            this.orbDialog = orbDialog;
            this.damageType = damageType;
        }
    }

    public Orb(Area area, DiscreteCoordinates position, OrbType orbType, DialogHandler handler) {
        super(area, Orientation.UP, position, orbType.element);
        this.element = orbType.element;
        this.damageType = orbType.damageType;
        this.spriteYDelta = orbType.spriteYDelta;
        this.orbDialog = orbType.orbDialog;

        this.dialogHandler = handler;


        for ( int i = 0; i < ANIMATION_FRAMES ; i ++) {
            sprites [i] = new RPGSprite("icoop/orb", 1, 1, this ,
                    new RegionOfInterest(i * 32 , spriteYDelta , 32 , 32) );
        }
        orbAnimation = new Animation ( ANIMATION_DURATION / ANIMATION_FRAMES , sprites );
    }

    public DamageType getDamageType() {
        return damageType;
    }

    @Override
    public void collect() {
        dialogHandler.publish(orbDialog);
        super.collect();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        orbAnimation.draw(canvas);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        orbAnimation.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (super.isSameElement(v)) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }

    @Override
    public Element element(){
        return element;
    }
}