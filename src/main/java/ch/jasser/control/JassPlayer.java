package ch.jasser.control;

import ch.jasser.entity.Card;
import ch.jasser.entity.Player;

import java.util.List;

public class JassPlayer {

    private final Player player;
    private final Game game;

    public JassPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public void receiveCard(Card card) {
        player.addCard(card);
    }

    public List<Card> getHandCards() {
        return player.getHand();
    }

    public void playCard(Card card) {
        if(player.removeCard(card)) {
            this.game.getCurrentTurn().playCard(card, this);
        } else {
            throw new RuntimeException(String.format("Cannot play this card [%s]", card));
        }
    }

    @Override
    public String toString() {
        return "JassPlayer{" +
                "player=" + player +
                '}';
    }
}
