package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponse;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
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

    private final GamesRepository repository;

    public HandOutCardsAction(GamesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActionResult act(Game game, JassPlayer player, JassRequest message) {
        List<Card> initialDeck = Schieber.getInitialDeck();
        List<JassPlayer> players = game.getPlayers();
        for (int i = 0; i < initialDeck.size(); i++) {
            players.get(i % players.size())
                   .receiveCard(initialDeck.get(i));
        }
        JassPlayer trumpPlayer =
                game.getPlayerByName(game.getTrumpPlayer())
                    .isPresent() ?
                        players.get(0) :
                        players.get((getTrumpPlayerIndex(players, game.getTrumpPlayer()) + 1) % players.size());

        JassResponses responses = new JassResponses();
        responses.addResponse(trumpPlayer.getName(),
                aJassResponse().withMessage(trumpPlayer.getName())
                               .withEvent(EventType.CHOOSE_TRUMP)
                               .build());
        responses.nextPlayer(List.of(trumpPlayer));
        for (JassPlayer currentPlayer : players) {
            JassResponse response = aJassResponse().withUsername(currentPlayer.getName())
                                                   .withEvent(EventType.RECEIVE_CARD)
                                                   .withCards(currentPlayer.getHand())
                                                   .build();
            responses.addResponse(currentPlayer.getName(), response);
            repository.addCardsToPlayer(game.getGameId(), currentPlayer.getName(), currentPlayer.getHand());
            repository.nextStep(game.getGameId(), GameStep.CHOOSE_TRUMP);
            repository.nextPlayer(game.getGameId(), List.of(trumpPlayer));
        }
        return new ActionResult(GameStep.CHOOSE_TRUMP, responses);
    }

    private int getTrumpPlayerIndex(List<JassPlayer> players, String trumpPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i)
                       .getName()
                       .equals(trumpPlayer)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public EventType getEventType() {
        return EventType.HAND_OUT_CARDS;
    }


}
