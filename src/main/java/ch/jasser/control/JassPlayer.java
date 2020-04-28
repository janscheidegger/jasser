package ch.jasser.control;

import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JassPlayer {

    private final Player player;
    private final Game game;
    private List<Card> hand = new ArrayList<>();


    public String getName() {
        return player.getName();
    }


    public JassPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHandCards() {
        return Collections.unmodifiableList(hand);
    }

    public boolean playCard(Card card) {
        if (hand.remove(card)) {
            return true;
        } else {
            throw new RuntimeException(String.format("Cannot play this card [%s]", card));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JassPlayer that = (JassPlayer) o;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public String toString() {
        return "JassPlayer{" +
                "player=" + player +
                '}';
    }
}
