package ch.jasser.control;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.JassSocket;
import ch.jasser.boundry.action.EventType;
import ch.jasser.boundry.payload.CardPlayedPayload;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.*;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Dependent
public class GameCoordinator {

    private final OpenGames openGames;
    private final JassSocket jassSocket;
    private final GamesRepository gamesRepository;
    private final Jsonb json = JsonbBuilder.create();

    public GameCoordinator(OpenGames openGames,
                           JassSocket jassSocket,
                           GamesRepository gamesRepository) {
        this.openGames = openGames;
        this.jassSocket = jassSocket;
        this.gamesRepository = gamesRepository;
    }

    public void handOutCard(String gameId, JassPlayer player, Card card) {
        player.receiveCard(card);

        JassMessage message = new JassMessage();
        message.setEvent(EventType.RECEIVE_CARD);
        message.setPayloadString(json.toJson(card));

        gamesRepository.handOutCard(gameId, player.getName(), card);

        jassSocket.sendToUser(player.getName(), message);
    }

    public String getWinningPlayer(Rules rules, List<PlayedCard> cardsOnTable, Suit currentSuit, Suit trump) {
        PlayedCard winningCard = rules.getWinningCard(cardsOnTable, currentSuit, trump);
        System.out.println(String.format("Card [%s] won round", winningCard.getCard()));
        return winningCard.getPlayer();
    }

    public void startGame(GameType type, Game game) {
        handOutCards(type, game);
    }

    public void joinGame(String gameId, String player) {
        Game gameState = getGameState(gameId);
        if (!gameState.getPlayers().contains(new JassPlayer(player))) {
            gamesRepository.addPlayer(gameId, new JassPlayer(player));
        }
    }

    private void handOutCards(GameType type, Game game) {
        if (type.equals(GameType.SCHIEBER)) {
            Rules gameRules = new Schieber();
            gamesRepository.addTurn(game.getGameId(), game.nextTurn());
            Map<JassPlayer, List<Card>> jassPlayerListMap = gameRules.handOutCards(gameRules.getInitialDeck(), game.getPlayers());
            for (Map.Entry<JassPlayer, List<Card>> list : jassPlayerListMap.entrySet()) {
                for (Card card : list.getValue()) {
                    handOutCard(game.getGameId(), list.getKey(), card);
                }
            }
        }
    }

    public void playCard(String username, String gameId, Card card) {
        Game game = openGames.getGame(gameId);
        Optional<JassPlayer> player = game.getPlayers().stream()
                .filter(p -> p.getName().equals(username))
                .findFirst();
        JassPlayer jassPlayer = player.orElseThrow(() -> new RuntimeException("Player not in Game"));
        if (jassPlayer.playCard(card)) {
            gamesRepository.addCardToTurn(gameId, jassPlayer, card, game.getTurns().size());
            gamesRepository.removeCardFromPlayer(gameId, jassPlayer, card);
            System.out.println(String.format("%s played %s", player, card));
        }

        game = openGames.getGame(gameId);
        if (game.getCurrentTurn().getCardsOnTable().size() == game.getPlayers().size()) {
            System.out.println("Turn is over, winner is");
            String winningPlayer = getWinningPlayer(new Schieber(), game.getCurrentTurn().getCardsOnTable(), Suit.HEARTS, Suit.HEARTS);

            gamesRepository.turnToWinningPlayer(gameId, winningPlayer);
            gamesRepository.addTurn(game.getGameId(), game.nextTurn());
        }

        JassMessage message = new JassMessage();
        message.setEvent(EventType.CARD_PLAYED);
        CardPlayedPayload payload = new CardPlayedPayload(card, jassPlayer.getName());
        message.setPayloadString(json.toJson(payload));
        jassSocket.sendToUsers(game.getPlayers().stream().map(JassPlayer::getName).collect(Collectors.toList()), message);
        /*if(firstCardOnTable()) {
            currentSuit = card.getSuit();
        }
        cardsOnTable.put(card, player);*/
    }

    public Game getGameState(String gameId) {
        return gamesRepository.findById(gameId);
    }
}
