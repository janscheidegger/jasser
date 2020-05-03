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
                         InitialLoadAction initialLoadAction,
                         PlayCardAction playCardAction,
                         HandOutCardsAction handOutCardsAction) {
        actionMap = Map.of(
                EventType.START_GAME, startGameAction,
                EventType.JOIN_GAME, joinGameAction,
                EventType.INITIAL_LOAD, initialLoadAction,
                EventType.HAND_OUT_CARDS, handOutCardsAction,
                EventType.PLAY_CARD, playCardAction
        );
    }

    Action<EmptyPayload> defaultAct = (username, gameId, payload) -> Optional.empty();


    public Optional<JassMessage> handleAction(String username, String gameId, JassMessage message) {
        System.out.println(String.format("[%s]", message.getEvent()));
        System.out.println("PAYLOAD: "+message.getPayloadString());
        var actionHandler = actionMap.getOrDefault(message.getEvent(), defaultAct);
        return actionHandler.act(username, gameId, message.getPayloadString());
    }
}
