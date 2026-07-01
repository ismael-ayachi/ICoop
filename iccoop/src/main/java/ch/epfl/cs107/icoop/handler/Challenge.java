package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.MultipleAnd;

public class Challenge implements Logic {
    private final MultipleAnd conditions;

    public Challenge(Logic ... conditions) {
        this.conditions = new MultipleAnd(conditions);
    }

    @Override
    public boolean isOn(){
        return conditions.isOn();
    }

    @Override
    public boolean isOff(){
        return conditions.isOff();
    }
}
