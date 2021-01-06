package ch.jasser.control.gamerules;

import ch.jasser.control.actions.Action;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;

import java.util.List;

public interface Rules {

    void registerAction(GameStep gameStep, Action action);

    PlayedCard getWinningCard(List<PlayedCard> cards, Suit currentSuit, Suit trump);

    Action getAllowedActionsForGameStep(GameStep step);

    int getPointsForCard(Card card, Suit trump);


    default int countPoints(List<Card> cards, Suit trump) {
        return cards.stream().mapToInt(c -> getPointsForCard(c, trump)).sum();
    }
}
