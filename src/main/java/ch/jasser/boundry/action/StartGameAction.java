package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.control.OpenGames;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.entity.Game;

import javax.enterprise.context.Dependent;
import java.util.Optional;

@Dependent
public class StartGameAction implements Action<EmptyPayload> {

    private OpenGames openGames;

    public StartGameAction(OpenGames openGames) {

        this.openGames = openGames;
    }

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Game game = new Game();
        this.openGames.addOpenGame(game);
        System.out.println("created game" + game.getGameId());
        return Optional.empty();
    }

}
