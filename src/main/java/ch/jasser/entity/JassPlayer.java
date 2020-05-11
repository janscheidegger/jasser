package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JassPlayer {

    private final String name;
    private List<Card> hand;
    private List<Card> cardsWon;

    public JassPlayer(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.cardsWon = new ArrayList<>();
    }

    @BsonCreator
    public JassPlayer(@BsonProperty("name") String name, @BsonProperty("hand") List<Card> hand, @BsonProperty("cardsWon") List<Card> cardsWon) {
        this.name = name;
        this.hand = hand;
        this.cardsWon = cardsWon;
    }


    public String getName() {
        return name;
    }


    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public List<Card> getCardsWon() {
        return cardsWon;
    }

    public boolean playCard(Card card) {
        if (hand.remove(card)) {
            return true;
        } else {
            throw new RuntimeException(String.format("Cannot play this card [%s], [%s]", card, hand));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JassPlayer that = (JassPlayer) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "JassPlayer{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}
