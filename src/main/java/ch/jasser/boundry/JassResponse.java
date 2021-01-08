package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;

import java.util.List;

public class JassResponse {
    private EventType event;
    private String username;
    private List<Card> hand;
    private List<Team> teams;
    private String message;
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

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Suit getChosenTrump() {
        return chosenTrump;
    }

    public void setChosenTrump(Suit chosenTrump) {
        this.chosenTrump = chosenTrump;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "JassResponse{" +
                "event=" + event +
                ", username='" + username + '\'' +
                ", hand=" + hand +
                ", message='" + message + '\'' +
                '}';
    }


    public static final class JassResponseBuilder {
        private JassResponse jassResponse;

        private JassResponseBuilder() {
            jassResponse = new JassResponse();
        }

        public static JassResponseBuilder aJassResponse() {
            return new JassResponseBuilder();
        }

        public JassResponseBuilder withEvent(EventType event) {
            jassResponse.setEvent(event);
            return this;
        }

        public JassResponseBuilder withTeams(List<Team> teams) {
            jassResponse.setTeams(teams);
            return this;
        }

        public JassResponseBuilder withMessage(String message) {
            jassResponse.setMessage(message);
            return this;
        }

        public JassResponseBuilder withUsername(String username) {
            jassResponse.setUsername(username);
            return this;
        }

        public JassResponseBuilder withHand(List<Card> hand) {
            jassResponse.setHand(hand);
            return this;
        }

        public JassResponseBuilder withChosenTrump(Suit trump) {
            jassResponse.setChosenTrump(trump);
            return this;
        }

        public JassResponse build() {
            return jassResponse;
        }
    }
}
