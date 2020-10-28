package ch.jasser.control;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassSocket;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.actions.Action;
import ch.jasser.control.actions.ActionResult;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class GameCoordinator {

    private final OpenGames openGames;
    private final JassSocket jassSocket;
    private final GamesRepository gamesRepository;
    private final Schieber schieber;
    private final Jsonb json = JsonbBuilder.create();

    public GameCoordinator(OpenGames openGames,
                           JassSocket jassSocket,
                           GamesRepository gamesRepository,
                           Schieber schieber) {
        this.openGames = openGames;
        this.jassSocket = jassSocket;
        this.gamesRepository = gamesRepository;

        this.schieber = schieber;
    }

    public void handOutCard(String gameId, JassPlayer player, Card card) {
        player.receiveCard(card);

        JassRequest message = new JassRequest();
        message.setEvent(EventType.RECEIVE_CARD);
        message.setCards(List.of(card));

        gamesRepository.handOutCard(gameId, player.getName(), card);

        jassSocket.sendToUser(player.getName(), message);
    }

    private String getWinningPlayer(Rules rules, List<PlayedCard> cardsOnTable, Suit currentSuit, Suit trump) {
        //    PlayedCard winningCard = rules.getWinningCard(cardsOnTable, currentSuit, trump);
        //    System.out.println(String.format("Card [%s] won round", winningCard.getCard()));
        //    return winningCard.getPlayer();
        return "";
    }

    public void joinGame(String gameId, String player) {
        Game gameState = getGameState(gameId);
        if (!gameState.getPlayers().contains(new JassPlayer(player))) {
            gamesRepository.addPlayer(gameId, new JassPlayer(player));
        }
    }

   /* private void handOutCards(GameType type, Game game) {
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
    } */

    public void playCard(String username, String gameId, Card card) {
        Game game = openGames.getGame(gameId);
        Optional<JassPlayer> player = game.getPlayers().stream()
                .filter(p -> p.getName().equals(username))
                .findFirst();
        JassPlayer jassPlayer = player.orElseThrow(() -> new RuntimeException("Player not in Game"));


        game = openGames.getGame(gameId);
        if (isLastCardInTurn(game)) {
            System.out.println("Turn is over, winner is");
            PlayedCard firstCard = game.getCurrentTurn().getCardsOnTable().get(0);
            String winningPlayer = getWinningPlayer(null, game.getCurrentTurn().getCardsOnTable(), firstCard.getCard().getSuit(), game.getTrump());

            gamesRepository.turnToWinningPlayer(gameId, winningPlayer);
            gamesRepository.addTurn(game.getGameId(), game.nextTurn());
        }
        JassRequest message = createCardPlayPayload(card, jassPlayer);
        jassSocket.sendToUsers(game.getPlayers().stream().map(JassPlayer::getName).collect(Collectors.toList()), message);
    }


    private JassRequest createCardPlayPayload(Card card, JassPlayer jassPlayer) {
        JassRequest message = new JassRequest();
        message.setEvent(EventType.CARD_PLAYED);
        message.setCards(List.of(card));
        return message;
    }

    private boolean isLastCardInTurn(Game game) {
        return !game.getCurrentTurn().getCardsOnTable().isEmpty() &&
                game.getCurrentTurn().getCardsOnTable().size() == game.getPlayers().size();
    }

    public Game getGameState(String gameId) {
        return gamesRepository.findById(gameId);
    }

    public ActionResult act(String gameId, String username, JassRequest message) {
        Game game = openGames.getGame(gameId);
        Optional<JassPlayer> player = game.getPlayers().stream()
                .filter(p -> p.getName().equals(username))
                .findFirst();
        JassPlayer jassPlayer = player.orElseThrow(() -> new RuntimeException("Player not in Game"));

        Action action = schieber.getAllowedActionsForGameStep(game.getStep());
        if (action.getEventType().equals(message.getEvent())) {
            return action.act(game, message);
        }
        throw new RuntimeException("Can not execute action");

    }


    public List<JassPlayer> getPlayers(String gameId) {
        return gamesRepository.findById(gameId).getPlayers();
    }
}
