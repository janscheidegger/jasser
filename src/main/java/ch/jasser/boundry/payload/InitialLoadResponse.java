package ch.jasser.boundry.payload;

import ch.jasser.entity.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialLoadResponse {
    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
