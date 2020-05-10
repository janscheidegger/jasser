package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

public class PlayedCard {
    private Card card;
    private String player;

    @BsonCreator
    public PlayedCard(@BsonProperty("card") Card card, @BsonProperty("player") String player) {
        this.card = card;
        this.player = player;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayedCard that = (PlayedCard) o;
        return Objects.equals(card, that.card) &&
                Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, player);
    }

    @Override
    public String toString() {
        return "PlayedCard{" +
                "car=" + card +
                ", player=" + player +
                '}';
    }
}
