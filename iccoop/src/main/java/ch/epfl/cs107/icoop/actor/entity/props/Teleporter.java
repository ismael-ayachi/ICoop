package ch.epfl.cs107.icoop.actor.entity.props;

import ch.epfl.cs107.icoop.handler.entity.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.MultipleAnd;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

/**
 * Represents a teleporter that allows players to move between areas when specific conditions are met.
 * The teleporter becomes active when all associated logic signals are true.
 */
public class Teleporter extends Door {

    private final RPGSprite sprite = new RPGSprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32, 32));
    private final MultipleAnd signals;

    /**
     * Creates a Teleporter entity.
     * @param area (Area): The area where the teleporter is located, not null
     * @param destination (String): The destination area name, not null
     * @param posPlayer1 (DiscreteCoordinates): Position for player 1 in the destination area
     * @param posPlayer2 (DiscreteCoordinates): Position for player 2 in the destination area
     * @param posCompanionPlayer1 (DiscreteCoordinates): Companion position for player 1 in the destination area
     * @param posCompanionPlayer2 (DiscreteCoordinates): Companion position for player 2 in the destination area
     * @param mainPos (DiscreteCoordinates): Main position of the teleporter in the current area
     * @param signals (Logic...): Logic signals that must all be true for the teleporter to activate
     */
    public Teleporter(Area area, String destination, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2,
                      DiscreteCoordinates posCompanionPlayer1, DiscreteCoordinates posCompanionPlayer2, DiscreteCoordinates mainPos, Logic ... signals) {
        super(area, destination, Logic.FALSE, posPlayer1, posPlayer2, posCompanionPlayer1, posCompanionPlayer2, mainPos);
        this.signals = new MultipleAnd(signals);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isActive()){
            sprite.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
    }

    @Override
    public boolean isActive(){
        return signals.isOn();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }
}
