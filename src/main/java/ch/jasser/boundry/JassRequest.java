package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

import java.util.List;

public class JassRequest {
    private EventType event;
    private String username;
    private List<Card> cards;
    private Suit chosenTrump;

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

    public Suit getChosenTrump() {
        return chosenTrump;
    }

    public void setChosenTrump(Suit chosenTrump) {
        this.chosenTrump = chosenTrump;
    }

    @Override
    public String toString() {
        return "JassMessage{" +
                "event='" + event + '\'' +
                '}';
    }


    public static final class JassRequestBuilder {
        private JassRequest jassRequest;

        private JassRequestBuilder() {
            jassRequest = new JassRequest();
        }

        public static JassRequestBuilder aJassRequest() {
            return new JassRequestBuilder();
        }

        public JassRequestBuilder withEvent(EventType event) {
            jassRequest.setEvent(event);
            return this;
        }

        public JassRequestBuilder withUsername(String username) {
            jassRequest.setUsername(username);
            return this;
        }

        public JassRequestBuilder withCards(List<Card> cards) {
            jassRequest.setCards(cards);
            return this;
        }

        public JassRequest build() {
            return jassRequest;
        }

        public JassRequestBuilder withChosenTrump(Suit suit) {
            jassRequest.setChosenTrump(suit);
            return this;
        }
    }
}
