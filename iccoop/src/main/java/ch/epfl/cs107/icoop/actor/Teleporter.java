package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
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

public class Teleporter extends Door {

    private final RPGSprite sprite = new RPGSprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32, 32));
    private final MultipleAnd signals;


    public Teleporter(Area area, String destination, DiscreteCoordinates posPlayer1, DiscreteCoordinates posPlayer2, DiscreteCoordinates mainPos, Logic ... signals) {
        super(area, destination, Logic.FALSE, posPlayer1, posPlayer2, mainPos);
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
