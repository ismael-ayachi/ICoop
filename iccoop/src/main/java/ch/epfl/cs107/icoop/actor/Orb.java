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
    private static int spriteYDelta;
    final Sprite[] sprites = new Sprite [ ANIMATION_FRAMES ];
    private final Animation orbAnimation;

    private Dialog orbDialog;

    private final Element element;
    private final DialogHandler dialogHandler;


    public Orb(Area area, DiscreteCoordinates position, Element element, DialogHandler handler) {
        super(area, Orientation.UP, position, element);
        this.element = element;
        dialogHandler = handler;

        if(element.equals(Element.FIRE)){
            spriteYDelta = 64;
            orbDialog = new Dialog("orb_fire_msg");
        }
        else if (element.equals(Element.WATER)){
            spriteYDelta = 0;
            orbDialog = new Dialog("orb_water_msg");
        }

        for ( int i = 0; i < ANIMATION_FRAMES ; i ++) {
            sprites [i] = new RPGSprite("icoop/orb", 1, 1, this ,
                    new RegionOfInterest(i * 32 , spriteYDelta , 32 , 32) );
        }
        orbAnimation = new Animation ( ANIMATION_DURATION / ANIMATION_FRAMES , sprites );
    }

    @Override
    public void collect() {
        super.collect();
        dialogHandler.publish(orbDialog);
        getOwnerArea().unregisterActor(this);
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