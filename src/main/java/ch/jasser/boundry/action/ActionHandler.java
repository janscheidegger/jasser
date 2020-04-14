package ch.jasser.boundry.action;

import javax.enterprise.context.Dependent;
import java.util.Map;

@Dependent
public class ActionHandler {

    private Map<String, Action> actionMap = Map.of("start", new StartGameAction());

    private static void defaultAct() {
        System.err.println("not defined");
    }


    public void handleAction(String action) {
        actionMap.getOrDefault(action, ActionHandler::defaultAct).act();
    }
}
