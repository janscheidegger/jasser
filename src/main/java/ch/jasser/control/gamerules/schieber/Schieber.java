package ch.jasser.control.gamerules.schieber;

import ch.jasser.entity.*;
import ch.jasser.control.gamerules.Rules;

import java.util.*;

public class Schieber implements Rules {

    private final SchieberCardRater cardRater = new SchieberCardRater();


    @Override
    public List<Card> getInitialDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
        return cards;
    }

    @Override
    public Map<JassPlayer, List<Card>> handOutCards(List<Card> initialDeck, List<JassPlayer> players) {
        Map<JassPlayer, List<Card>> cardsPerPlayer = new HashMap<>();
        for (int i = 0; i < initialDeck.size(); i++) {
            cardsPerPlayer.computeIfAbsent(players.get(i % players.size()), k -> new ArrayList<>()).add(initialDeck.get(i));
        }
        return cardsPerPlayer;
    }

    @Override
    public PlayedCard getWinningCard(List<PlayedCard> cards, Suit currentSuit, Suit trump) {
        cards.sort(Comparator.comparingInt(o -> cardRater.getValue(currentSuit, trump, o.getCard())));
        return cards.get(cards.size()-1);
    }
}
