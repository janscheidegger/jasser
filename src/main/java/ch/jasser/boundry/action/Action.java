package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.Payload;

import java.util.Optional;

public interface Action<T extends Payload> {
    Optional<JassMessage> act(String username, String gameId, String payload);
}
