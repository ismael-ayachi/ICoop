package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
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

/**
 * Represents a magical staff with elemental properties, which can be collected and interacts with specific entities.
 * The staff includes an animation and specific behaviors based on its elemental type.
 */
public class Staff extends ElementalItem {

    private static final int ANIMATION_DURATION = 32;
    private static final int ANIMATION_FRAMES = 4;
    final Sprite[] sprites = new Sprite [ANIMATION_FRAMES];
    private final Animation staffanimation;
    private final Element element;

    /**
     * Defines the types of staffs, each with a unique sprite and elemental attribute.
     */
    public enum StaffType {
        WATER_STAFF("icoop/staff_water", Element.WATER),
        FIRE_STAFF("icoop/staff_fire", Element.FIRE);

        private final String name;
        private final Element element;

        /**
         * Creates a StaffType with a sprite name and an element.
         * @param name (String): Name of the sprite resource
         * @param element (Element): Elemental type of the staff
         */
        StaffType(String name, Element element) {
            this.name = name;
            this.element = element;
        }
    }

    /**
     * Creates a Staff entity with a specific type and position in the area.
     * @param area (Area): The area where the staff is located, not null
     * @param position (DiscreteCoordinates): The position of the staff in the area, not null
     * @param stafftype (StaffType): The type of the staff, defining its element and appearance
     */
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