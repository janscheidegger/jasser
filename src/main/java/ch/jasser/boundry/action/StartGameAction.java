package ch.jasser.boundry.action;

import ch.jasser.control.OpenGames;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.entity.Game;

import javax.enterprise.context.Dependent;

@Dependent
public class StartGameAction implements Action<EmptyPayload> {

    private OpenGames openGames;

    public StartGameAction(OpenGames openGames) {

        this.openGames = openGames;
    }

    @Override
    public void act(String payload) {
        Game game = new Game();
        this.openGames.addOpenGame(game);
        System.out.println("created game" + game.getGameId());
    }

}
