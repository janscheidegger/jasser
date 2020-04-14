package ch.jasser.control;

import ch.jasser.control.gamerules.Rules;
import ch.jasser.entity.Card;
import ch.jasser.entity.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Turn {

    private Map<Card, JassPlayer> cardsOnTable = new HashMap<>();
    private final Rules rules;
    private Suit trump;
    private Suit currentSuit;

    public Turn(Rules rules, Suit trump) {
        this.rules = rules;
        this.trump = trump;
    }

    public void playCard(Card card, JassPlayer player) {
        System.out.println(String.format("%s played %s", player, card));
        if(firstCardOnTable()) {
            currentSuit = card.getSuit();
        }
        cardsOnTable.put(card, player);
    }

    private boolean firstCardOnTable() {
        return cardsOnTable.entrySet().size() == 0;
    }

    public JassPlayer getWinningPlayer() {
        Card winningCard = rules.getWinningCard(new ArrayList<>(cardsOnTable.keySet()), currentSuit, trump);
        return cardsOnTable.get(winningCard);
    }

}
