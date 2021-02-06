package ch.jasser.control;

import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

//@ApplicationScoped
public class GamesRepository {

    private final MongoClient mongoClient;

    public GamesRepository(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void createGame(Game game) {
        getCollection().insertOne(toDocument(game));
    }

    List<Game> getAll() {
        List<Game> games = new ArrayList<>();
        try (MongoCursor<Game> iterator = getCollection().find(Game.class)
                                                         .iterator()) {
            while (iterator.hasNext()) {
                Game game = iterator.next();

                games.add(game);
            }
        }
        return games;
    }

    void addPlayer(String gameId, JassPlayer player) {
        getCollection().updateOne(eq("gameId", gameId), Updates.addToSet("players", player));
    }

    void addTurn(String gameId, Turn turn) {
        getCollection().updateOne(eq("gameId", gameId), Updates.push("turns", turn));
    }

    private Document toDocument(Game game) {
        return new Document()
                .append("gameId", game.getGameId())
                .append("type", game.getType())
                .append("players", Collections.emptyList())
                .append("turns", Collections.emptyList());
    }

    private MongoCollection<Document> getCollection() {
        return this.mongoClient.getDatabase("jasser")
                               .getCollection("games");
    }

    public Game findById(String uuid) {
        return getCollection().find(eq("gameId", uuid), Game.class)
                              .first();
    }

    public void handOutCard(String gameId, String name, Card card) {
        getCollection().updateOne(and(eq("gameId", gameId), eq("players.name", name)), Updates.addToSet("players.$.hand", card));
    }

    public void removeCardFromPlayer(String gameId, String jassPlayer, Card card) {
        getCollection().updateOne(
                and(
                        eq("gameId", gameId),
                        eq("players.name", jassPlayer)),
                Updates.pull("players.$.hand", card));
    }

    public void addCardToTurn(String gameId, String jassPlayer, Card card, int turn) {
        PlayedCard playedCard = new PlayedCard(card, jassPlayer);
        getCollection().updateOne(
                eq("gameId", gameId),
                and(
                        Updates.push("turns." + (turn - 1) + ".cardsOnTable", playedCard)
                )
        );
    }

    public Game turnToWinningPlayer(String gameId, String winningPlayer) {
        List<PlayedCard> cardsOnTable = findById(gameId).getCurrentTurn()
                                                        .getCardsOnTable();
        List<Card> cards = cardsOnTable.stream()
                                       .map(PlayedCard::getCard)
                                       .collect(Collectors.toList());
        getCollection().updateOne(and(eq("gameId", gameId),
                eq("players.name", winningPlayer)),
                Updates.addEachToSet("players.$.cardsWon", cards));
        return findById(gameId);
    }

    public void setTrump(String gameId, Suit trump) {
        throw new RuntimeException("Not implemented yet");
    }

    public void setTeams(String gameId, List<Team> teams) {
        throw new RuntimeException("Not implemented yet");
    }

    public void addPointsToTeam(String gameId, Team team, int teamPoints) {
        throw new RuntimeException("Not implemented yet");
    }

    public void adjustSittingOrder(String gameId, Game game) {
        throw new RuntimeException("Not yet implemented");
    }

    public void nextPlayer(String gameId, List<JassPlayer> nextPlayer) {
        throw new RuntimeException("Not yet implemented");
    }

    public void nextStep(String gameId, GameStep gameStep) {
        throw new RuntimeException("Not yet implemented");
    }

    public void addCardsToPlayer(String gameId, String playerName, List<Card> cards) {
        throw new RuntimeException("Not yet implemented");
    }

    public void nextTurn(String gameId) {
        throw new RuntimeException("Not yet implemented");
    }
}
