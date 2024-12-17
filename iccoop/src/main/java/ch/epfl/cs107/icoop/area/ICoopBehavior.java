package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.AreaCellTypeHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICoopBehavior extends AreaBehavior {
    /**
     * Default ICoopBehavior Constructor
     *
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */
    public ICoopBehavior(Window window, String name, AreaCellTypeHandler cellTypeHandler) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
                cellTypeHandler.addCellTypeActor(color, new DiscreteCoordinates(x, y));
            }
        }
    }


    public enum ICoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0,false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true , true),
        OBSTACLE (-16723187, true , true)
        ;

        final int type;
        final boolean canWalk;
        final boolean canFly;

        ICoopCellType(int type, boolean canWalk , boolean canFly) {
            this.type = type;
            this.canFly = canFly;
            this.canWalk = canWalk;

        }

        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            //System.out.println(type);
            return NULL;
        }
    }



    /**
     * Cell adapted to the ICoop game
     */
    public class ICoopCell extends Cell {
        /// Type of the cell following the enum
        private final ICoopCellType type;

        /**
         * Default ICoopCell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */

        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity instanceof Unstoppable) return true;

            for (Interactable i : entities) {
                if (i.takeCellSpace() && entity.takeCellSpace()) {
                    return false;
                }
                else if (i instanceof ElementalWall) {
                    if (entity instanceof ElementalEntity){
                        return ((ElementalWall) i).element().equals(((ElementalEntity)entity).element()) || ((ElementalWall) i).isDisabled();
                    }
                    return ((ElementalWall) i).isDisabled();
                }
                else if (entity instanceof Bomb && i instanceof Bomb){
                    return false;
                }
                else if (entity instanceof Projectile){
                    return type.canFly;
                }


            }
            return type.canWalk;
        }



        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction ( AreaInteractionVisitor v , boolean isCellInteraction ) {
            ((ICoopInteractionVisitor) v). interactWith( this ,isCellInteraction );

        }

    }
}
