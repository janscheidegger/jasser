package ch.jasser.control.actions;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.JassResponse;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Turn;

import javax.enterprise.context.Dependent;

@Dependent
public class PlayCardAction implements Action {

    private GamesRepository repository;

    public PlayCardAction(GamesRepository repository) {

        this.repository = repository;
    }

    @Override
    public ActionResult act(Game game, JassMessage message) {
        // TODO: get actual player
        if(isAllowedToPlayCard(game, game.getPlayers().get(0), message.getCards().get(0))) {
            repository.addCardToTurn(game.getGameId(), message.getUsername(), message.getCards().get(0), game.getTurns().size());
            repository.removeCardFromPlayer(game.getGameId(), message.getUsername(), message.getCards().get(0));
        }

        return new ActionResult(GameStep.MOVE, new JassResponse());
        // TODO: Error handling
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
