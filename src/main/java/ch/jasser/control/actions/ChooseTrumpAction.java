package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

public class ChooseTrumpAction implements Action {

    @Override
    public ActionResult act(Game game, JassRequest message) {
        if (message.getChosenTrump() != null) {
            game.setTrump(message.getChosenTrump());
            return new ActionResult(GameStep.PRE_TURN, new JassResponses().addResponse("",
                    aJassResponse().withEvent(EventType.TRUMP_CHOSEN)
                            .withChosenTrump(message.getChosenTrump())
                            .withUsername(message.getUsername()).build())
            );
        } else {
            return new ActionResult(GameStep.PRE_ROUND, new JassResponses().addResponse("",
                    aJassResponse().withEvent(EventType.SCHIEBEN).withUsername(message.getUsername()).build())
            );
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.CHOOSE_TRUMP;
    }
}
