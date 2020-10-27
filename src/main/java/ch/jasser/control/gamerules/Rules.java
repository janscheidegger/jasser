package ch.jasser.control.gamerules;

import ch.jasser.control.actions.Action;
import ch.jasser.control.steps.GameStep;

public interface Rules {

    Action getActionsForGameStep(GameStep step);
}
