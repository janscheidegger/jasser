package ch.jasser.boundry;

import ch.jasser.control.OpenGames;
import ch.jasser.entity.Game;
import org.eclipse.yasson.internal.JsonBindingBuilder;

import javax.annotation.PostConstruct;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jass/{username}/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JassRessource {

    private OpenGames openGames;
    private Jsonb jsonb = JsonbBuilder.create();

    public JassRessource(OpenGames openGames) {

        this.openGames = openGames;
    }

    @POST
    public Response createGame() {
        Game game = new Game();
        openGames.addOpenGame(game);
        return Response.ok(jsonb.toJson(game)).build();
    }

    @GET
    public Response listGames(@PathParam("username") String username) {
        return Response.ok(jsonb.toJson(openGames.getOpenGames()))
                .build();
    }
}
