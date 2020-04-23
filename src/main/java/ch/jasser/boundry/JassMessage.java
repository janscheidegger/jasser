package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;

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

    @Override
    public String toString() {
        return "JassMessage{" +
                "event='" + event + '\'' +
                '}';
    }
}
