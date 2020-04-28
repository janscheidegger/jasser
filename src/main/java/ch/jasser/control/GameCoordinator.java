package ch.jasser.control;

import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class GameCoordinator {

    private OpenGames openGames;

    public GameCoordinator(OpenGames openGames) {
        this.openGames = openGames;
    }

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

    public void playCard(String username, String gameId, Card card) {
        Game game = openGames.getGame(UUID.fromString(gameId));
        Optional<JassPlayer> player = game.getPlayers().stream()
                .filter(p -> p.getName().equals(username))
                .findFirst();
        JassPlayer jassPlayer = player.orElseThrow(() -> new RuntimeException("Player not in Game"));
        if (jassPlayer.playCard(card)) {
            game.getCurrentTurn().addCard(jassPlayer, card);
        }
        System.out.println(String.format("%s played %s", player, card));
        /*if(firstCardOnTable()) {
            currentSuit = card.getSuit();
        }
        cardsOnTable.put(card, player);*/
    }

}
