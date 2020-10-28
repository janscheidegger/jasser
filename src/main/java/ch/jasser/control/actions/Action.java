package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.entity.Game;

public interface Action {

    ActionResult act(Game game, JassRequest message);
    EventType getEventType();
}
