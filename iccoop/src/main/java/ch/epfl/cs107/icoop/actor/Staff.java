package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Staff extends ElementalItem {

    private static final int ANIMATION_DURATION = 32;
    private static final int ANIMATION_FRAMES = 4;
    final Sprite[] sprites = new Sprite [ANIMATION_FRAMES];
    private final Animation staffanimation;

    private final Element element;

    public enum StaffType {
        WATER_STAFF("icoop/staff_water", Element.WATER),
        FIRE_STAFF("icoop/staff_fire", Element.FIRE);

        private final String name;
        private final Element element;

        StaffType(String name, Element element) {
            this.name = name;
            this.element = element;
        }
    }

    public Staff(Area area, DiscreteCoordinates position, StaffType stafftype) {
        super(area,Orientation.DOWN, position, stafftype.element);
        this.element = stafftype.element;

        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite(stafftype.name, 2, 2, this, new RegionOfInterest(i *
                    32, 0, 32, 32), new Vector(-0.5f, 0));
        }
        staffanimation = new Animation( ANIMATION_DURATION / ANIMATION_FRAMES , sprites );
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isCollected()) {
            staffanimation.draw(canvas);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isCollected()){
            getOwnerArea().unregisterActor(this);
        }
        else {
            staffanimation.update(deltaTime);
        }
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ElementalEntity){
            if(isSameElement((ElementalEntity) v)) {
                ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
            }
        }
    }

    @Override
    public Element element() {
        return element;
    }

}
