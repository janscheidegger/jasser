package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.entity.Game;

public class HandOutCardsAction implements Action {

    @Override
    public ActionResult act(Game game, JassRequest message) {
        return null;
    }

    @Override
    public EventType getEventType() {
        return EventType.HAND_OUT_CARDS;
    }
}
