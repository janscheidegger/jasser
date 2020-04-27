package ch.jasser.control;

import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dependent
public class GameCoordinator {
    public void handOutCard(JassPlayer player, Card card) {
        player.receiveCard(card);
    }

    public JassPlayer getWinningPlayer(Rules rules, Map<Card, JassPlayer> cardsOnTable, Suit currentSuit, Suit trump) {
        Card winningCard = rules.getWinningCard(new ArrayList<>(cardsOnTable.keySet()), currentSuit, trump);
        return cardsOnTable.get(winningCard);
    }

    public void startGame(GameType type, Game game) {
        handOutCards(type, game);
    }

    private void handOutCards(GameType type, Game game) {
        if (type.equals(GameType.SCHIEBER)) {
            Rules gameRules = new Schieber(this);
            gameRules.handOutCards(gameRules.getInitialDeck(), game.getPlayers());
        }
    }

    public void playCard(Game game, Card card, JassPlayer player) {
        if (player.playCard(card)) {
            game.getCurrentTurn().addCard(player, card);
        }
        System.out.println(String.format("%s played %s", player, card));
        /*if(firstCardOnTable()) {
            currentSuit = card.getSuit();
        }
        cardsOnTable.put(card, player);*/
    }

}
