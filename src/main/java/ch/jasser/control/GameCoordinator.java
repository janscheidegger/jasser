package ch.jasser.control;

import ch.jasser.entity.Card;

public class GameCoordinator {
    public void handOutCard(JassPlayer player, Card card) {
        player.receiveCard(card);
    }
}
