package ch.jasser.boundry;

public class JassMessage {
    private String event;
    private String payloadString;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
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
