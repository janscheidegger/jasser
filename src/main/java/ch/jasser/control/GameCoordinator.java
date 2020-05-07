package ch.jasser.control;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.JassSocket;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.*;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;

@Dependent
public class GameCoordinator {

    private OpenGames openGames;
    private JassSocket jassSocket;
    private GamesRepository gamesRepository;

    public GameCoordinator(OpenGames openGames,
                           JassSocket jassSocket,
                           GamesRepository gamesRepository) {
        this.openGames = openGames;
        this.jassSocket = jassSocket;
        this.gamesRepository = gamesRepository;
    }

    public void handOutCard(String gameId, JassPlayer player, Card card) {
        player.receiveCard(card);

        Jsonb json = JsonbBuilder.create();

        JassMessage message = new JassMessage();
        message.setEvent(EventType.RECEIVE_CARD);
        message.setPayloadString(json.toJson(card));

        gamesRepository.handOutCard(gameId, player.getName(), card);

        jassSocket.sendToUser(player.getName(), message);
    }

    public JassPlayer getWinningPlayer(Rules rules, Map<Card, JassPlayer> cardsOnTable, Suit currentSuit, Suit trump) {
        Card winningCard = rules.getWinningCard(new ArrayList<>(cardsOnTable.keySet()), currentSuit, trump);
        return cardsOnTable.get(winningCard);
    }

    public void startGame(GameType type, Game game) {
        handOutCards(type, game);
    }

    public void joinGame(String gameId, String player) {
        gamesRepository.addPlayer(gameId, new JassPlayer(player));
    }

    private void handOutCards(GameType type, Game game) {
        if (type.equals(GameType.SCHIEBER)) {
            Rules gameRules = new Schieber();
            Map<JassPlayer, List<Card>> jassPlayerListMap = gameRules.handOutCards(gameRules.getInitialDeck(), game.getPlayers());
            for (Map.Entry<JassPlayer, List<Card>> list : jassPlayerListMap.entrySet()) {
                for (Card card : list.getValue()) {
                    handOutCard(game.getGameId(), list.getKey(), card);
                }
            }
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
