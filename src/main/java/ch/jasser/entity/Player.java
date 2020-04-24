package ch.jasser.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public boolean removeCard(Card card) {
        return hand.remove(card);
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    @Override
    public String toString() {
        return "Player{" +
                ", name='" + name + '\'' +
                '}';
    }
}
