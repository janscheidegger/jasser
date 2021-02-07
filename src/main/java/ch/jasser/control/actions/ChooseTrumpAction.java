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
    public JassResponses act(Game game, JassPlayer player, JassRequest message) {
        if (message.getEvent()
                   .equals(EventType.CHOOSE_TRUMP) && message.getChosenTrump() != null) {
            repository.setTrump(game.getGameId(), message.getChosenTrump());
            repository.nextStep(game.getGameId(), GameStep.MOVE);
            return new JassResponses(GameStep.MOVE).addResponse(game.getPlayerNames(),
                    aJassResponse().withEvent(EventType.TRUMP_CHOSEN)
                                   .withChosenTrump(message.getChosenTrump())
                                   .withUsername(player.getName())
                                   .build()
            );
        } else {
            return new JassResponses(GameStep.CHOOSE_TRUMP).addResponse(game.getPlayerNames(),
                    aJassResponse().withEvent(EventType.SCHIEBEN)
                                   .withUsername(message.getUsername())
                                   .build()
            );
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.CHOOSE_TRUMP;
    }
}
