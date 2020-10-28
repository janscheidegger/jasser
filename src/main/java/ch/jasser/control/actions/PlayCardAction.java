package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponse;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Turn;

import javax.enterprise.context.Dependent;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@Dependent
public class PlayCardAction implements Action {

    private final GamesRepository repository;

    public PlayCardAction(GamesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActionResult act(Game game, JassRequest message) {
        JassPlayer player = game.getPlayerByName(message.getUsername()).orElseThrow(RuntimeException::new);
        Card card = message.getCards().get(0);
        if (isAllowedToPlayCard(game, player, card)) {
            playCard(game, player, card);
            JassResponse response = aJassResponse().withEvent(EventType.CARD_PLAYED)
                    .withHand(player.getHand())
                    .build();
            return new ActionResult(GameStep.MOVE, new JassResponses().addResponse(message.getUsername(), response));
        } else {
            JassResponse response = aJassResponse().withEvent(EventType.ERROR)
                    .withUsername(message.getUsername())
                    .build();
            return new ActionResult(GameStep.MOVE, new JassResponses().addResponse(message.getUsername(), response));
        }
    }

    private void playCard(Game game, JassPlayer player, Card card) {
        repository.addCardToTurn(game.getGameId(), player.getName(), card, game.getTurns().size());
        repository.removeCardFromPlayer(game.getGameId(), player.getName(), card);
        player.getHand().remove(card);

    }

    @Override
    public EventType getEventType() {
        return EventType.PLAY_CARD;
    }

    private boolean isAllowedToPlayCard(Game game, JassPlayer jassPlayer, Card card) {
        if (!jassPlayer.getHand().contains(card)) {
            return false;
        }
        Turn currentTurn = game.getCurrentTurn();
        return currentTurn.getCardsOnTable().size() == 0
                || currentTurn.getPlayedSuit().equals(card.getSuit())
                || (card.getSuit().equals(game.getTrump()));
    }
}
