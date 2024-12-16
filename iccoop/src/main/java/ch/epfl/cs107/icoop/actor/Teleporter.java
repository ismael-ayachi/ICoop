package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Teleporter extends Door implements Logic {

    private RPGSprite teleporterSprite = new RPGSprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32, 32));
    private Logic logic;


    public Teleporter(Area area, String destination, Logic signal, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates mainPos) {
        super(area, destination, signal, posPlayer1, posPlayer2, mainPos);
        this.logic = signal;
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        teleporterSprite.draw(canvas);
    }


    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this ,isCellInteraction );
    }

    @Override
    public boolean isOn() {
        return logic.isOn();
    }

    @Override
    public boolean isOff() {
        return logic.isOff();
    }
}
