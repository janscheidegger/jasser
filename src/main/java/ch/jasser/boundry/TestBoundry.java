package ch.jasser.boundry;

import ch.jasser.control.Game;
import ch.jasser.control.JassPlayer;
import ch.jasser.entity.Player;

public class TestBoundry {
    public static void main(String[] args) {

        Game game = new Game();

        JassPlayer p1 = game.joinGame(new Player("p1"));
        JassPlayer p2 = game.joinGame(new Player("p2"));
        JassPlayer p3 = game.joinGame(new Player("p3"));
        JassPlayer p4 = game.joinGame(new Player("p4"));

        game.startGame();
        game.nextTurn();

        p1.playCard(p1.getHandCards().get(0));
        p2.playCard(p2.getHandCards().get(0));
        p3.playCard(p3.getHandCards().get(0));
        p4.playCard(p4.getHandCards().get(0));

        game.endCurrentTurn();

    }

}
