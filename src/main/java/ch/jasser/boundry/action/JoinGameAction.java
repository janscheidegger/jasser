package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.control.OpenGames;
import ch.jasser.boundry.payload.JoinGamePayload;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class JoinGameAction implements Action<JoinGamePayload> {

    private OpenGames openGames;

    public JoinGameAction(OpenGames openGames) {
        this.openGames = openGames;
    }

    @Override
    public Optional<JassMessage> act(String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        JoinGamePayload joingGame = jsonb.fromJson(payload, JoinGamePayload.class);
        openGames.getGame(joingGame.getGameId()).addPlayers(joingGame.getPlayer());
        return Optional.empty();
    }

}
