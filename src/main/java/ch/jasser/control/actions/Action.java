package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;

public interface Action {

    ActionResult act(Game game, JassPlayer username, JassRequest message);
    EventType getEventType();
}
