package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.control.GameCoordinator;
import ch.jasser.boundry.payload.JoinGamePayload;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class JoinGameAction implements Action<JoinGamePayload> {

    private GameCoordinator coordinator;

    public JoinGameAction(GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        JoinGamePayload joinGame = jsonb.fromJson(payload, JoinGamePayload.class);
        coordinator.joinGame(gameId, username);
        return Optional.empty();
    }

}
