package ch.jasser.control.actions;

import ch.jasser.boundry.JassMessage;
import ch.jasser.entity.Game;

public interface Action {

    ActionResult act(Game game, JassMessage message);
}
