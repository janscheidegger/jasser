package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponse;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;

import javax.enterprise.context.Dependent;
import java.util.List;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@Dependent
public class HandOutCardsAction implements Action {

    @Override
    public ActionResult act(Game game, JassRequest message) {
        List<Card> initialDeck = Schieber.getInitialDeck();
        List<JassPlayer> players = game.getPlayers();
        for (int i = 0; i < initialDeck.size(); i++) {
            players.get(i % players.size()).receiveCard(initialDeck.get(i));
        }

        JassResponses responses = new JassResponses();
        for (JassPlayer player : players) {
            JassResponse response = aJassResponse().withUsername(player.getName())
                    .withEvent(EventType.RECEIVE_CARD)
                    .withHand(player.getHand())
                    .build();
            responses.addResponse(player.getName(), response);
        }
        return new ActionResult(GameStep.PRE_MOVE, responses);
    }

    @Override
    public EventType getEventType() {
        return EventType.HAND_OUT_CARDS;
    }
}
