package ch.jasser.boundry;

import ch.jasser.control.OpenGames;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/jass/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JassRessource {

    private OpenGames openGames;
    private MongoClient mongoClient;
    private Jsonb jsonb = JsonbBuilder.create();

    public JassRessource(OpenGames openGames, MongoClient mongoClient) {

        this.openGames = openGames;
        this.mongoClient = mongoClient;
    }

    @Path("test")
    @GET
    public Response test() {
        Game game = new Game();
        Document document = new Document()
                .append("gameId", game.getGameId())
                .append("type", game.getType());

        mongoClient.getDatabase("jass")
                .getCollection("games")
                .insertOne(document);
        return Response.ok().build();
    }

    @GET
    @Path("getTest")
    public Response getTest() {
        List<Game> games = new ArrayList<>();
        try (MongoCursor<Document> cursor = mongoClient.getDatabase("jass")
                .getCollection("games")
                .find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Game game = new Game(document.get("gameId", UUID.class), GameType.valueOf(document.get("type", String.class)));

                games.add(game);
            }
        }
        return Response.ok(jsonb.toJson(games)).build();
    }

    @POST
    public Response createGame() {
        Game game = new Game();
        openGames.addOpenGame(game);
        return Response.ok(jsonb.toJson(game)).build();
    }

    @GET
    public Response listGames() {
        return Response.ok(jsonb.toJson(openGames.getOpenGames()))
                .build();
    }
}
