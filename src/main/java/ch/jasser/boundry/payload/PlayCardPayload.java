package ch.jasser.boundry.payload;

import ch.jasser.entity.Card;

public class PlayCardPayload implements Payload {
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
