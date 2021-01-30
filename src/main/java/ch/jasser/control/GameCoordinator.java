package ch.jasser.control;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.JassSocket;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.actions.Action;
import ch.jasser.control.actions.ActionResult;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Optional;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@Dependent
public class GameCoordinator {

    private final OpenGames openGames;
    private final JassSocket jassSocket;
    private final GamesRepository gamesRepository;
    private final Rules schieber;
    private final Jsonb json = JsonbBuilder.create();

    public GameCoordinator(OpenGames openGames,
                           JassSocket jassSocket,
                           GamesRepository gamesRepository,
                           Rules schieber) {
        this.openGames = openGames;
        this.jassSocket = jassSocket;
        this.gamesRepository = gamesRepository;

        this.schieber = schieber;
    }

    public void joinGame(String gameId, String player) {
        Game gameState = getGameState(gameId);
        if (!gameState.getPlayers()
                      .contains(new JassPlayer(player))) {
            gamesRepository.addPlayer(gameId, new JassPlayer(player));
        }
    }

    public Game getGameState(String gameId) {
        return gamesRepository.findById(gameId);
    }

    public ActionResult act(String gameId, String username, JassRequest message) {
        Game game = openGames.getGame(gameId);
        Optional<JassPlayer> player = game.getPlayers()
                                          .stream()
                                          .filter(p -> p.getName()
                                                        .equals(username))
                                          .findFirst();
        JassPlayer jassPlayer = player.orElseThrow(() -> new RuntimeException("Player not in Game"));

        if (!game.getMoveAllowed()
                 .contains(username)) {
            return new ActionResult(game.getStep(),
                    new JassResponses().addResponse(username,
                            aJassResponse().withUsername(username)
                                           .withEvent(EventType.ERROR)
                                           .withMessage(String.format("%s not allowed for %s", message.getEvent(),
                                                   username))
                                           .build()));
        }

        Action action = schieber.getAllowedActionsForGameStep(game.getStep());
        if (action == null) {
            throw new RuntimeException("No Action defined for " + game.getStep());
        }

        if (action.getEventType()
                  .equals(message.getEvent())) {
            return action.act(game, jassPlayer, message);
        }
        throw new RuntimeException(String.format("Can not execute action [ActionEventType=%s, RequestEventType=%s, gameStep=%s]",
                action.getEventType(), message.getEvent(), game.getStep()));

    }

    public List<JassPlayer> getPlayers(String gameId) {
        return gamesRepository.findById(gameId)
                              .getPlayers();
    }
}
