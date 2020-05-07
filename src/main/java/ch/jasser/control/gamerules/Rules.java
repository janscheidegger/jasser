package ch.jasser.control.gamerules;

import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

import java.util.List;
import java.util.Map;

public interface Rules {

    List<Card> getInitialDeck();

    Map<JassPlayer, List<Card>> handOutCards(List<Card> initialDeck, List<JassPlayer> players);

    Card getWinningCard(List<Card> cards, Suit currentSuit, Suit trump);
}
