package ch.jasser.control.gamerules.schieber;

import ch.jasser.control.gamerules.CardRater;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

public class SchieberCardRater implements CardRater {

    private static final int TRUMP_MULTIPLIER = 100;
    private static final int CURRENT_SUIT_MULTIPLIER = 10;

    @Override
    public int getValue(Suit currentSuit, Suit trump, Card card) {
        if (card.getSuit().equals(trump)) {
            return card.getRank().getValue() * TRUMP_MULTIPLIER;
        }
        return rateCardBySuit(currentSuit, card);
    }

    private int rateCardBySuit(Suit currentSuit, Card card) {
        if (card.getSuit().equals(currentSuit)) {
            return card.getRank().getValue() * CURRENT_SUIT_MULTIPLIER;
        } else {
            return card.getRank().getValue();
        }
    }
}
