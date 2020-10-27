package ch.jasser.control.actions;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.JassResponse;
import ch.jasser.control.steps.GameStep;

public class ActionResult {

    private GameStep nextStep;
    private JassResponse response;

    public ActionResult(GameStep nextStep, JassResponse response) {
        this.nextStep = nextStep;
        this.response = response;
    }

    public GameStep getNextStep() {
        return nextStep;
    }

    public JassResponse getResponse() {
        return response;
    }
}
