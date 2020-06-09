package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;
import ch.jasser.boundry.payload.ErrorPayload;

public class JassMessage {
    private EventType event;
    private String payloadString;

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getPayloadString() {
        return payloadString;
    }

    public void setPayloadString(String payloadString) {
        this.payloadString = payloadString;
    }

    public static JassMessage error(String error) {
        JassMessage message = new JassMessage();
        message.setPayloadString(error);
        message.setEvent(EventType.ERROR);
        return message;
    }

    @Override
    public String toString() {
        return "JassMessage{" +
                "event='" + event + '\'' +
                '}';
    }
}
