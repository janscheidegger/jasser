package ch.jasser.control;

import ch.jasser.entity.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import javax.enterprise.context.Dependent;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Dependent
public class GamesRepository {

    private MongoClient mongoClient;

    public GamesRepository(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    void createGame(Game game) {
        getCollection().insertOne(toDocument(game));
    }

    List<Game> getAll() {
        List<Game> games = new ArrayList<>();
        try (MongoCursor<Game> iterator = getCollection().find(Game.class).iterator()) {
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
        getCollection().updateOne(eq("gameId", gameId), Updates.addToSet("turns", turn));
    }

    private Document toDocument(Game game) {
        return new Document()
                .append("gameId", game.getGameId())
                .append("type", game.getType())
                .append("players", Collections.emptyList())
                .append("turns", Collections.emptyList());
    }

    private MongoCollection<Document> getCollection() {
        return this.mongoClient.getDatabase("jasser").getCollection("games");
    }

    public Game findById(String uuid) {
        return getCollection().find(eq("gameId", uuid), Game.class).first();
    }

    public void handOutCard(String gameId, String name, Card card) {
        getCollection().updateOne(and(eq("gameId", gameId), eq("players.name", name)), Updates.addToSet("players.$.hand", card));
    }

    public void removeCardFromPlayer(String gameId, JassPlayer jassPlayer, Card card) {
        getCollection().updateOne(
                and(
                        eq("gameId", gameId),
                        eq("players.name", jassPlayer.getName())),
                Updates.pull("players.$.hand", card));
    }

    public void addCardToTurn(String gameId, JassPlayer jassPlayer, Card card, int turn) {
        PlayedCard playedCard = new PlayedCard(card, jassPlayer.getName());
        getCollection().updateOne(
                eq("gameId", gameId),
                Updates.addToSet("turns." + (turn - 1) + ".cardsOnTable", playedCard)
        );
    }
}
