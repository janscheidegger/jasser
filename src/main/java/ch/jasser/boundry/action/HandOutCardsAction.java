package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.control.GameCoordinator;
import ch.jasser.control.OpenGames;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;

import javax.enterprise.context.Dependent;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class HandOutCardsAction implements Action<EmptyPayload> {

    private GameCoordinator coordinator;
    private OpenGames openGames;

    public HandOutCardsAction(GameCoordinator coordinator, OpenGames openGames) {
        this.coordinator = coordinator;
        this.openGames = openGames;
    }

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Game game = openGames.getGame(UUID.fromString(gameId));
        coordinator.startGame(GameType.SCHIEBER, game);
        return Optional.empty();
    }
}
