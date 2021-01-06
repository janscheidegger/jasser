package ch.jasser.control.gamerules.schieber;

import ch.jasser.control.actions.Action;
import ch.jasser.control.actions.PlayCardAction;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.*;
import ch.jasser.control.gamerules.Rules;

import javax.enterprise.context.Dependent;
import java.util.*;

public class Schieber implements Rules {

    private final SchieberCardRater cardRater = new SchieberCardRater();
    private Map<GameStep, Action> actionMap = new HashMap<>();

    @Override
    public void registerAction(GameStep gameStep, Action action) {
        actionMap.put(gameStep, action);
    }

    public static List<Card> getInitialDeck() {
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
    public PlayedCard getWinningCard(List<PlayedCard> cards, Suit currentSuit, Suit trump) {
        cards.sort(Comparator.comparingInt(o -> cardRater.getValue(currentSuit, trump, o.getCard())));
        return cards.get(cards.size()-1);
    }

    @Override
    public Action getAllowedActionsForGameStep(GameStep step) {
        return actionMap.get(step);
    }

    @Override
    public int getPointsForCard(Card card, Suit trump) {
        return card.getRank().getPoints(card.getSuit().equals(trump));
    }
}
