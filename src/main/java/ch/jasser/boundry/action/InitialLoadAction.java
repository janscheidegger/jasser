package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.boundry.payload.InitialLoadResponse;
import ch.jasser.control.GameCoordinator;
import ch.jasser.entity.*;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class InitialLoadAction implements Action<EmptyPayload> {

    private GameCoordinator coordinator;

    public InitialLoadAction(GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        InitialLoadResponse initialLoadResponse = new InitialLoadResponse();

        Game game = coordinator.getGameState(gameId);

        JassPlayer player = game.getPlayers().stream()
                .filter(p -> username.equals(p.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Could not find player %s", username)));

        initialLoadResponse.setCards(player.getHand());

        JassMessage response = new JassMessage();
        response.setEvent(EventType.INITIAL_LOAD);
        response.setPayloadString(jsonb.toJson(initialLoadResponse));
        System.out.println("INITIALLOAD: " + response);
        return Optional.of(response);
    }
}
