package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
import ch.epfl.cs107.icoop.actor.entity.props.Bomb;
import ch.epfl.cs107.icoop.actor.entity.props.ElementalWall;
import ch.epfl.cs107.icoop.actor.entity.Unstoppable;
import ch.epfl.cs107.icoop.actor.entity.projectile.Projectile;
import ch.epfl.cs107.icoop.handler.utilities.AreaCellTypeHandler;
import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

/**
 * Represents the behavior of an ICoop area, defining the properties and interactions of each cell.
 */
public class ICoopBehavior extends AreaBehavior {

    /**
     * Default constructor for ICoopBehavior.
     * Initializes cells based on the color-coded behavior map and registers corresponding actors.
     *
     * @param window (Window): The game window, not null.
     * @param name (String): The name of the behavior file, not null.
     * @param cellTypeHandler (AreaCellTypeHandler): Handler to add actors based on cell types, not null.
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

    /**
     * Enum defining different cell types with properties for walkability and flight compatibility.
     */
    public enum ICoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0,false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true , true),
        OBSTACLE (-16723187, true , true),
        GRASS (-5675521, true, true );

        final int type;
        final boolean canWalk;
        final boolean canFly;

        /**
         * Constructor for ICoopCellType.
         *
         * @param type (int): RGB value representing the cell type.
         * @param canWalk (boolean): Indicates if entities can walk on this cell.
         * @param canFly (boolean): Indicates if entities can fly over this cell.
         */
        ICoopCellType(int type, boolean canWalk , boolean canFly) {
            this.type = type;
            this.canFly = canFly;
            this.canWalk = canWalk;

        }

        /**
         * Converts an RGB value to the corresponding ICoopCellType.
         *
         * @param type (int): RGB value.
         * @return (ICoopCellType): The corresponding cell type.
         */
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
         * Default constructor for ICoopCell.
         *
         * @param x (int): x-coordinate of the cell.
         * @param y (int): y-coordinate of the cell.
         * @param type (ICoopCellType): The type of the cell, not null.
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
                else if (entity instanceof Bomb) {
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
