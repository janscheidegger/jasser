package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.boundry.payload.Payload;

import javax.enterprise.context.Dependent;
import java.util.Map;

@Dependent
public class ActionHandler {

    private Map<String, Action<? extends Payload>> actionMap;

    public ActionHandler(StartGameAction startGameAction,
                         JoinGameAction joinGameAction) {
        actionMap = Map.of(
                "start", startGameAction,
                "join", joinGameAction
        );
    }

    Action<EmptyPayload> defaultAct = payload -> System.out.println("default");


    public void handleAction(JassMessage message) {
        var actionHandler = actionMap.getOrDefault(message.getEvent(), defaultAct);
        actionHandler.act(message.getPayloadString());
    }
}
