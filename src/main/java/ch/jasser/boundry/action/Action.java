package ch.jasser.boundry.action;

import ch.jasser.boundry.payload.Payload;

public interface Action<T extends Payload> {

    void act(String payload);

}
