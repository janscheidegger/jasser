package ch.jasser.control.gamerules;

import ch.jasser.control.JassPlayer;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

import java.util.Collection;
import java.util.List;

public interface Rules {

    List<Card> getInitialDeck();

    void handOutCards(List<Card> initialDeck, List<JassPlayer> players);

    Card getWinningCard(List<Card> cards, Suit currentSuit, Suit trump);
}
