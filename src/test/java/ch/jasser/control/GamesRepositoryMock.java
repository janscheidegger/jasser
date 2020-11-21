package ch.jasser.control;

import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Turn;
import com.mongodb.client.MongoClient;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mock
public class GamesRepositoryMock extends GamesRepository {

    private Map<String, Game> games = new HashMap<>();

    public GamesRepositoryMock(MongoClient mongoClient) {
        super(null);
    }

    @Override
    public void createGame(Game game) {
        this.games.put(game.getGameId(), game);
    }

    @Override
    List<Game> getAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    void addPlayer(String gameId, JassPlayer player) {
        games.get(gameId).getPlayers().add(player);
    }

    @Override
    void addTurn(String gameId, Turn turn) {
        games.get(gameId).getTurns().add(turn);
    }

    @Override
    public Game findById(String uuid) {
        return games.get(uuid);
    }

    @Override
    public void handOutCard(String gameId, String name, Card card) {
        games.get(gameId).getPlayerByName(name).ifPresent(p -> p.receiveCard(card));
    }

    @Override
    public void removeCardFromPlayer(String gameId, String jassPlayer, Card card) {
        games.get(gameId).getPlayerByName(jassPlayer).ifPresent(p -> p.playCard(card));
    }

    @Override
    public void addCardToTurn(String gameId, String jassPlayer, Card card, int turn) {
        games.get(gameId).getCurrentTurn().getCardsOnTable().add(new PlayedCard(card, jassPlayer));
    }

    @Override
    public void turnToWinningPlayer(String gameId, String winningPlayer) {
        List<Card> cardsOnTable = games.get(gameId).getCurrentTurn()
                .getCardsOnTable().stream().map(PlayedCard::getCard).collect(Collectors.toList());

        games.get(gameId).getPlayerByName(winningPlayer).ifPresent(p -> p.getCardsWon().addAll(cardsOnTable));
    }
}
