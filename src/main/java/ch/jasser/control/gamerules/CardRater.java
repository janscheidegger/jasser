package ch.jasser.control.gamerules;

import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

public interface CardRater {

    int getValue(Suit currentSuit, Suit trump, Card card);

}
