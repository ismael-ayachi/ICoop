    package ch.epfl.cs107.icoop.actor.entity.player;

    import ch.epfl.cs107.icoop.KeyBindings;
    import ch.epfl.cs107.icoop.actor.collectables.*;
    import ch.epfl.cs107.icoop.actor.decor.Grass;
    import ch.epfl.cs107.icoop.actor.entity.props.Mage;
    import ch.epfl.cs107.icoop.actor.elemental.DamageType;
    import ch.epfl.cs107.icoop.actor.elemental.ElementalEntity;
    import ch.epfl.cs107.icoop.actor.entity.projectile.ElementalBall;
    import ch.epfl.cs107.icoop.actor.entity.Health;
    import ch.epfl.cs107.icoop.actor.entity.foe.Foe;
    import ch.epfl.cs107.icoop.actor.entity.props.*;
    import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
    import ch.epfl.cs107.icoop.handler.entity.TargetEntity;
    import ch.epfl.cs107.icoop.handler.item.ICoopInventory;
    import ch.epfl.cs107.icoop.handler.item.ICoopItem;
    import ch.epfl.cs107.icoop.handler.player.ICoopPlayerStatusGUI;
    import ch.epfl.cs107.icoop.handler.player.ManorDoorPlayerView;
    import ch.epfl.cs107.icoop.handler.utilities.Timer;
    import ch.epfl.cs107.play.areagame.actor.Interactable;
    import ch.epfl.cs107.play.areagame.actor.Interactor;
    import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
    import ch.epfl.cs107.play.areagame.area.Area;
    import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
    import ch.epfl.cs107.play.areagame.handler.Inventory;
    import ch.epfl.cs107.play.areagame.handler.InventoryItem;
    import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
    import ch.epfl.cs107.play.math.DiscreteCoordinates;
    import ch.epfl.cs107.play.math.Orientation;
    import ch.epfl.cs107.play.math.Transform;
    import ch.epfl.cs107.play.math.Vector;
    import ch.epfl.cs107.play.window.Button;
    import ch.epfl.cs107.play.window.Keyboard;

    import java.util.Collections;
    import java.util.List;
    import static ch.epfl.cs107.play.math.Orientation.*;

    /**
     * Represents a player entity in the ICoop game.
     * The player can interact with the environment, fight enemies, use items, and manage a companion.
     */
    public final class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Inventory.Holder, TargetEntity, ManorDoorPlayerView {
        private final Element element;
        private DamageType immunity;
        private final PlayerType playerType;
        private ICoopCompanion companion;

        private final static int MAX_LIFE = 5;
        private final Health hp;
        private final static int IFRAMES = 24;
        private final Timer invincibilityTimer;

        private final static int WALK_ANIMATION_DURATION = 4;
        private final static int SWORD_ANIMATION_DURATION = 2;
        private final static int STAFF_ANIMATION_DURATION = 2;
        private final static int MOVE_DURATION = 8;
        private final ICoopPlayerStatusGUI statusGUI;

        private static final Orientation[] walkOrders = {DOWN, RIGHT, UP, LEFT};

        
        private static final Orientation[] attackOrders = {DOWN , UP, RIGHT , LEFT};
        private final OrientedAnimation walkAnimation;
        private final OrientedAnimation swordAnimation;
        private final OrientedAnimation staffAnimation;
        private final KeyBindings.PlayerKeyBindings keys;

        private final ICoopPlayerInteractionHandler handler;

        private final ICoopInventory inventory;
        private ICoopItem currentItem;
        private int currentItemIndex;

        private Door currentDoor;
        private boolean doorIsPassed;
        private PlayerState currentState;

        /**
         * Enum for player states.
         */
        public enum PlayerState {
            IDLE, ATTACKING_WITH_SWORD, ATTACKING_WITH_STAFF
        }

        /**
         * Enum for player types with their specific properties.
         */
        public enum PlayerType {
            RED_PLAYER("icoop/player", ".staff_fire", Element.FIRE, ICoopCompanion.CompanionType.RED_COMPANION,  KeyBindings.RED_PLAYER_KEY_BINDINGS, false),
            BLUE_PLAYER("icoop/player2", ".staff_water", Element.WATER, ICoopCompanion.CompanionType.BLUE_COMPANION, KeyBindings.BLUE_PLAYER_KEY_BINDINGS, true);

            public final String prefix;
            public final String staffName;
            public final Element element;
            public final ICoopCompanion.CompanionType companionType;
            public final KeyBindings.PlayerKeyBindings keys;
            public final boolean posGUI;


            PlayerType(String prefix, String staffName, Element element, ICoopCompanion.CompanionType companionType, KeyBindings.PlayerKeyBindings keys, boolean posGUI){
                this.prefix = prefix;
                this.staffName = staffName;
                this.element = element;
                this.companionType = companionType;
                this.keys = keys;
                this.posGUI = posGUI;
            }
        }

        /**
         * Creates a player.
         * @param owner (Area): Area to which the player belongs.
         * @param orientation (Orientation): Initial orientation of the player.
         * @param coordinates (DiscreteCoordinates): Initial position on the grid.
         * @param playerType (PlayerType): Type of the player.
         */
        public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, PlayerType playerType) {
            super(owner, orientation, coordinates);

            this.element = playerType.element;
            this.keys = playerType.keys;
            this.playerType = playerType;


            this.walkAnimation = new OrientedAnimation(playerType.prefix, WALK_ANIMATION_DURATION, this,
                    new Vector(0, 0), walkOrders, 4, 1, 2, 16, 32, true);

            this.swordAnimation = new OrientedAnimation(playerType.prefix + ".sword", SWORD_ANIMATION_DURATION, this,
                    new Vector(-.5f, 0), attackOrders, 4, 2, 2, 32, 32);
            this.staffAnimation = new OrientedAnimation(playerType.prefix + playerType.staffName, STAFF_ANIMATION_DURATION, this,
                    new Vector(-.5f, -.20f), attackOrders, 4, 2, 2, 32, 32);

            this.currentState = PlayerState.IDLE;
            this.hp = new Health(this,Transform.I.translated(0,1.75f),MAX_LIFE,true);

            this.invincibilityTimer = new Timer();

            this.handler = new ICoopPlayerInteractionHandler();

            this.inventory= new ICoopInventory("playerPocket");
            this.statusGUI = new ICoopPlayerStatusGUI(playerType.posGUI);
            
            resetMotion();
        }

        /**
         * Updates the player's state and behavior for each game frame.
         * Handles animations, keyboard inputs, item usage, and transitions between states.
         * @param deltaTime (float): elapsed time since the last frame, in seconds.
         */
        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            switch (currentState) {
                case IDLE: {
                    if (isDisplacementOccurs()) {
                        walkAnimation.update(deltaTime);
                    }
                    Keyboard keyboard = getOwnerArea().getKeyboard();
                    moveIfPressed(LEFT, keyboard.get(keys.left()));
                    moveIfPressed(UP, keyboard.get(keys.up()));
                    moveIfPressed(RIGHT, keyboard.get(keys.right()));
                    moveIfPressed(DOWN, keyboard.get(keys.down()));

                    invincibilityTimer.tick();

                    if (keyboard.get(keys.switchItem()).isPressed()) {
                        switchItem();
                    } else if (keyboard.get(keys.useItem()).isPressed()) {
                        useCurrentItem();
                    }
                    break;
                }
                case ATTACKING_WITH_SWORD: {
                    swordAnimation.update(deltaTime);
                    if (swordAnimation.isCompleted()) {
                        currentState = PlayerState.IDLE;
                        swordAnimation.reset();
                    }
                    break;
                }
                case ATTACKING_WITH_STAFF: {
                    staffAnimation.update(deltaTime);
                    if (staffAnimation.isCompleted()) {
                        currentState = PlayerState.IDLE;
                        staffAnimation.reset();
                    }
                    break;
                }
            }
            statusGUI.setCurrentItem(currentItem);
        }
        
        @Override
        public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
            if (invincibilityTimer.interval(3)) {
                switch (currentState) {
                    case IDLE: walkAnimation.draw(canvas); break;
                    case ATTACKING_WITH_SWORD: swordAnimation.draw(canvas); break;
                    case ATTACKING_WITH_STAFF: staffAnimation.draw(canvas); break;
                }
            }
            hp.draw(canvas);
            statusGUI.draw(canvas);
        }

        @Override
        public boolean takeCellSpace() {
            return true;
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
        public List<DiscreteCoordinates> getCurrentCells() {
            return Collections.singletonList(getCurrentMainCellCoordinates());
        }

        @Override
        public DiscreteCoordinates getCurrentMainCellCoordinates() {
            return super.getCurrentMainCellCoordinates();
        }

        @Override
        public boolean wantsCellInteraction() {
            return true;
        }

        @Override
        public boolean wantsViewInteraction() {
            Keyboard keyboard = getOwnerArea().getKeyboard();
            return keyboard.get(keys.useItem()).isPressed();

        }

        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
            other.acceptInteraction(handler,isCellInteraction);
        }

        public Door getCurrentDoor(){
            return currentDoor;
        }

        public boolean isDoorPassed(){
            return doorIsPassed;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

        }

        @Override
        public List<DiscreteCoordinates> getFieldOfViewCells() {
            return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        }

        /**
         * Orientate and Move this player in the given orientation if the given button is down
         *
         * @param orientation (Orientation): given orientation, not null
         * @param b           (Button): button corresponding to the given orientation, not null
         */
        
        private void moveIfPressed(Orientation orientation, Button b) {
            if (b.isDown()) {
                if (!isDisplacementOccurs()) {
                    orientate(orientation);
                    move(MOVE_DURATION);
                }
            }
        }

        /**
         * Leave an area by unregister this player
         */
        public void leaveArea() {
            getOwnerArea().unregisterActor(this);
            if(companion != null){
                companion.leaveArea();
            }
        }

        /**
         * makes the player entering a given area
         *
         * @param area     (Area):  the area to be entered, not null
         * @param position (DiscreteCoordinates): initial position in the entered area, not null
         * @param companionPosition (DiscreteCoordinates): initial position in the entered area, not null
         *
         */

        public void enterArea(Area area, DiscreteCoordinates position, DiscreteCoordinates companionPosition) {
            area.registerActor(this);
            setOwnerArea(area);
            setCurrentPosition(position.toVector());
            resetMotion();
            if (companion != null) {
                companion.enterArea(area, companionPosition);
            }

            doorIsPassed = false;
        }

        /**
         * Center the camera on the player
         */

        @Override
        public ElementalEntity.Element element() {
            return this.element;
        }


        /**
         * Resets the player's health to the maximum value.
         * This ensures that the player is fully healed.
         */
        public void resetHealth() {
            hp.resetHealth();
        }

        /**
         * Checks if the player is currently invincible.
         * Invincibility is determined by whether the invincibility timer is active.
         * @return true if the player is invincible, false otherwise
         */
        public boolean invincible() {
            return invincibilityTimer.isGoing();
        }

        /**
         * Reduces the player's health by the specified damage amount if the player is not invincible
         * and the damage type does not match the player's immunity.
         * Method starts the invincibility timer after taking damage.
         * @param damageType (DamageType): the type of damage inflicted
         * @param damage (int): the amount of damage to be subtracted from the player's health
         */
        public void damage(DamageType damageType, int damage) {
            if (damageType!=immunity && !invincible() && hp.isOn()) {
                invincibilityTimer.start(IFRAMES);
                hp.decrease(damage);
            }
        }


        /**
         * Determines if the player is dead by checking if their health is depleted.
         * @return true if the player's health is zero, false otherwise
         */
        public boolean isDead() {
            return hp.isOff();
        }


        @Override
        public boolean possess(InventoryItem item){
            return inventory.contains(item);
        }

        /**
         * Switches the player's current item to the next available one in their inventory.
         * Cycles through all available items in the inventory and sets the first valid one as the current item.
         */
        public void switchItem() {
            boolean switchItem = false;
            for (int i=0; i<ICoopItem.values().length; i++) {
                currentItemIndex++;
                currentItemIndex %= ICoopItem.values().length;
                if (possess(ICoopItem.values()[currentItemIndex])){
                    switchItem = true;
                    break;
                }
            }
            if(switchItem)
                currentItem = ICoopItem.values()[currentItemIndex];
        }

        /**
         * Uses the currently selected item, executing its specific functionality based on its type.
         * If the player no longer possesses the current item, it is set to null.
         */
        public void useCurrentItem(){
            if (currentItem!=null && !isDisplacementOccurs()) {
                if (possess(currentItem)) {
                    switch (currentItem) {
                        case BOMB: {
                            if (placeBomb()) inventory.removePocketItem(currentItem, 1);
                            break;
                        }
                        case SWORD: {
                            currentState = PlayerState.ATTACKING_WITH_SWORD;
                            break;
                        }
                        case FIRE_STAFF: {
                            currentState = PlayerState.ATTACKING_WITH_STAFF;
                            spawnElementalBall(ElementalBall.BallType.FIRE_BALL);
                            break;
                        }
                        case WATER_STAFF: {
                            currentState = PlayerState.ATTACKING_WITH_STAFF;
                            spawnElementalBall(ElementalBall.BallType.WATER_BALL);
                            break;
                        }
                        default : break;
                    }
                }
                if (!possess(currentItem)) currentItem = null;
            }
        }

        /**
         * Places a bomb in the player's field of view if the area allows for it.
         * @return true if the bomb was successfully placed and registered in the area, false otherwise
         */
        public boolean placeBomb() {
            Bomb placedBomb = new Bomb(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst(), Bomb.DEFAULT_BOMB_TIMER);
            if (getOwnerArea().canEnterAreaCells(placedBomb, getFieldOfViewCells())) {
                return getOwnerArea().registerActor(placedBomb);
            }
            return false;
        }

        /**
         * Spawns an elemental ball of the specified type at the player's location,
         * moving in the player's current orientation.
         * @param ballType (ElementalBall.BallType): the type of elemental ball to spawn
         */
        public void spawnElementalBall(ElementalBall.BallType ballType) {
            ElementalBall ball = new ElementalBall(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), ballType);
            getOwnerArea().registerActor(ball);
        }

        /**
         * Sets the player to have passed through the specified door if the door is active.
         * @param door (Door): the door being passed through
         */
        public void passDoor(Door door){
            if (door.isActive()) {
                currentDoor = door;
                doorIsPassed = true;
            }
        }

        /**
         * Picks up a specified item and quantity, adding it to the player's inventory.
         * Sets the current item to the picked-up item.
         * @param item (ICoopItem): the item to pick up
         * @param quantity (int): the number of items to pick up
         */
        public void pickUpItem(ICoopItem item, int quantity){
            inventory.addPocketItem(item, quantity);
            currentItem = item;
        }

        /**
         * Spawns the player's companion in the area near the player.
         * Registers the companion as an actor in the area.
         */
        public void spawnCompanion(){
            this.companion = new ICoopCompanion(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(-1,0),
                    playerType.companionType, this);
            getOwnerArea().registerActor(companion);
        }

        /**
         * Heals the player by increasing their health points by the specified amount.
         * @param amount (int): the amount of health to restore
         */
        public void heal(int amount){
            hp.increase(amount);
        }

        /**
         * Handles interactions between the player and other interactable objects.
         * Defines specific behaviors for each interaction type.
         */
        private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor, ElementalEntity {

            @Override
            public void interactWith(Door door, boolean isCellInteraction) {
                if (door.isActive() && isCellInteraction) {
                    passDoor(door);
                }
            }

            @Override
            public void interactWith(Teleporter teleporter, boolean isCellInteraction) {
                if (teleporter.isActive() && isCellInteraction) {
                    passDoor(teleporter);
                }

            }

            @Override
            public void interactWith(ManorDoor manorDoor, boolean isCellInteraction) {
                if (isCellInteraction) {
                    passDoor(manorDoor);
                    manorDoor.publish();
                    manorDoor.setPlayerView(ICoopPlayer.this);
                }
            }

            @Override
            public void interactWith(Grass grass, boolean isCellInteraction){
                if(!isCellInteraction && currentState == PlayerState.ATTACKING_WITH_SWORD)
                    grass.cut();
            }

            @Override
            public void interactWith(Bomb bomb, boolean isCellInteraction) {
                if (isCellInteraction) {
                    inventory.addPocketItem(ICoopItem.BOMB,1);
                    bomb.collect();
                } else {
                    bomb.activate();
                }
            }

            @Override
            public void interactWith(Orb orb, boolean isCellInteraction) {
                if (isCellInteraction) {
                    orb.collect();
                    immunity = orb.getDamageType();
                    spawnCompanion();
                }
            }

            @Override
            public void interactWith(EvolutionPotion potion, boolean isCellInteraction) {
                if (isCellInteraction) {
                    potion.collect();
                    companion.evolve();
                }
            }

            @Override
            public void interactWith(Heart heart, boolean isCellInteraction) {
                if (isCellInteraction) {
                    heart.collect();
                    heal(1);
                }
            }

            @Override
            public void interactWith(PressurePlate plate, boolean isCellInteraction) {
                if (isCellInteraction) {
                    plate.step(ICoopPlayer.this);
                }
            }

            @Override
            public void interactWith(Lever lever, boolean isCellInteraction){
                if (!isCellInteraction)
                    lever.pull();
            }

            @Override
            public void interactWith(Staff staff, boolean isCellInteraction) {
                if (isCellInteraction) {
                    if(staff.element().equals(element)) {
                        staff.collect();
                        switch (staff.element()){
                            case FIRE : {
                                pickUpItem(ICoopItem.FIRE_STAFF, 1);
                                break;
                            }
                            case WATER : {
                                pickUpItem(ICoopItem.WATER_STAFF, 1);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void interactWith(Foe foe, boolean isCellInteraction) {
                if (!isCellInteraction && currentState == PlayerState.ATTACKING_WITH_SWORD) {
                    foe.damage(DamageType.PHYSICAL,1);
                }
            }

            @Override
            public void interactWith(Key key, boolean isCellInteraction) {
                if (isCellInteraction) {
                    if(key.element().equals(element)) {
                        key.collect();
                        switch (key.element()){
                            case FIRE : {
                                pickUpItem(ICoopItem.FIRE_KEY, 1);
                                break;
                            }
                            case WATER : {
                                pickUpItem(ICoopItem.WATER_KEY, 1);
                                break;
                            }
                        }

                    }
                }
            }
            @Override
            public void interactWith(Chest chest, boolean isCellInteraction) {
                if (!isCellInteraction) {
                    chest.open(ICoopPlayer.this);
                }
            }

            @Override
            public void interactWith(Mage mage, boolean isCellInteraction) {
                if (!isCellInteraction) {
                    mage.publish();
                }
            }
            @Override
            public Element element() {
                return element;
            }
        }
    }

