package ch.jasser.control.actions;

import ch.jasser.boundry.JassResponses;
import ch.jasser.control.steps.GameStep;

public class ActionResult {

    private GameStep nextStep;
    private JassResponses response;

    public ActionResult(GameStep nextStep, JassResponses response) {
        this.nextStep = nextStep;
        this.response = response;
    }

    public GameStep getNextStep() {
        return nextStep;
    }

    public JassResponses getResponse() {
        return response;
    }
}
