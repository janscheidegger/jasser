package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.boundry.payload.Payload;

import javax.enterprise.context.Dependent;
import java.util.Map;
import java.util.Optional;

@Dependent
public class ActionHandler {

    private Map<EventType, Action<? extends Payload>> actionMap;

    public ActionHandler(StartGameAction startGameAction,
                         JoinGameAction joinGameAction,
                         InitialLoadAction initialLoadAction) {
        actionMap = Map.of(
                EventType.START_GAME, startGameAction,
                EventType.JOIN_GAME, joinGameAction,
                EventType.INITIAL_LOAD, initialLoadAction
        );
    }

    Action<EmptyPayload> defaultAct = payload -> Optional.empty();


    public Optional<JassMessage> handleAction(JassMessage message) {
        var actionHandler = actionMap.getOrDefault(message.getEvent(), defaultAct);
        return actionHandler.act(message.getPayloadString());
    }
}
