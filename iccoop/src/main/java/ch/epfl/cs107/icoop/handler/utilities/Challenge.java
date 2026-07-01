package ch.epfl.cs107.icoop.handler.utilities;

import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.MultipleAnd;

/**
 * Represents a challenge grouping multiple logical conditions.
 * The challenge is considered active if all conditions are met.
 */
public class Challenge implements Logic {
    private final MultipleAnd conditions;

    /**
     * Creates a Challenge with multiple logical conditions.
     * @param conditions (Logic...): The conditions that must be met for the challenge to be active.
     */
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
