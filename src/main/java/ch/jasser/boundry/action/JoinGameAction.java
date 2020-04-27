package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.control.JassPlayer;
import ch.jasser.control.OpenGames;
import ch.jasser.boundry.payload.JoinGamePayload;
import ch.jasser.entity.Game;
import ch.jasser.entity.Player;

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
        JoinGamePayload joinGame = jsonb.fromJson(payload, JoinGamePayload.class);
        Game openGame = openGames.getGame(joinGame.getGameId());
        JassPlayer jassPlayer = new JassPlayer(new Player(joinGame.getPlayer()), openGame);
        openGames.getGame(joinGame.getGameId()).joinGame(new Player(joinGame.getPlayer()));
        return Optional.empty();
    }

}
