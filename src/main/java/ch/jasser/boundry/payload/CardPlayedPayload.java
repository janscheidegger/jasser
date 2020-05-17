package ch.jasser.boundry.payload;

import ch.jasser.entity.Card;

import java.util.Objects;

public class CardPlayedPayload {
    private Card card;
    private String player;

    public CardPlayedPayload(Card card, String player) {
        this.card = card;
        this.player = player;
    }

    public Card getCard() {
        return card;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "CardPlayedPayload{" +
                "card=" + card +
                ", player='" + player + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardPlayedPayload that = (CardPlayedPayload) o;
        return Objects.equals(card, that.card) &&
                Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, player);
    }
}
