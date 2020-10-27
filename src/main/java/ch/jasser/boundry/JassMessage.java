package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;
import ch.jasser.boundry.payload.ErrorPayload;
import ch.jasser.entity.Card;

import java.util.List;

public class JassMessage {
    private EventType event;
    private String username;
    private List<Card> cards;

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "JassMessage{" +
                "event='" + event + '\'' +
                '}';
    }
}
