package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;

import javax.enterprise.context.Dependent;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@Dependent
public class ChooseTrumpAction implements Action {

    private GamesRepository repository;

    public ChooseTrumpAction(GamesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActionResult act(Game game, JassPlayer player, JassRequest message) {
        if (message.getChosenTrump() != null) {
            repository.setTrump(game.getGameId(), message.getChosenTrump());
            return new ActionResult(GameStep.PRE_TURN, new JassResponses().addResponse("",
                    aJassResponse().withEvent(EventType.TRUMP_CHOSEN)
                            .withChosenTrump(message.getChosenTrump())
                            .withUsername(player.getName()).build())
            );
        } else {
            return new ActionResult(GameStep.CHOOSE_TRUMP, new JassResponses().addResponse("",
                    aJassResponse().withEvent(EventType.SCHIEBEN).withUsername(message.getUsername()).build())
            );
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.CHOOSE_TRUMP;
    }
}
