package ch.jasser.control.gamerules.schieber;

import ch.jasser.control.GameCoordinator;
import ch.jasser.control.JassPlayer;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.entity.Card;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Schieber implements Rules {

    private final GameCoordinator coordinator;
    private final SchieberCardRater cardRater = new SchieberCardRater();

    public Schieber(GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

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
    public void handOutCards(List<Card> initialDeck, List<JassPlayer> players) {
        for (int i = 0; i < initialDeck.size(); i++) {
            coordinator.handOutCard(players.get(i % players.size()), initialDeck.get(i));
        }
    }

    @Override
    public Card getWinningCard(List<Card> cards, Suit currentSuit, Suit trump) {
        cards.sort(Comparator.comparingInt(o -> cardRater.getValue(currentSuit, trump, o)));
        return cards.get(cards.size()-1);
    }
}
