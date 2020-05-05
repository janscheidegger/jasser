package ch.jasser.control;

import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.Player;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.enterprise.context.Dependent;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

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
        try (MongoCursor<Document> iterator = getCollection().find().iterator()) {
            while (iterator.hasNext()) {
                Document doc = iterator.next();
                Game game = gameFromDocument(doc);
                games.add(game);
            }
        }
        return games;
    }

    void addPlayer(UUID gameId, Player player) {
        getCollection().updateOne(eq("gameId", gameId.toString()), set("players", Collections.singletonList(player.getName())));
    }

    private Game gameFromDocument(Document doc) {
        Game game = new Game(UUID.fromString(doc.get("gameId", String.class)), GameType.valueOf(doc.get("type", String.class)));
        List<String> players = doc.getList("players", String.class);
        for (String player : players) {
            game.addPlayer(player);
        }
        return game;
    }

    private Document toDocument(Game game) {
        return new Document()
                .append("gameId", game.getGameId().toString())
                .append("type", game.getType());
    }

    private MongoCollection<Document> getCollection() {
        return this.mongoClient.getDatabase("jasser").getCollection("games");
    }

    public Game findById(UUID uuid) {
        Document game = getCollection().find(eq("gameId", uuid.toString())).first();
        return gameFromDocument(game);
    }
}
