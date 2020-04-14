package ch.jasser.control;

import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Player;
import ch.jasser.entity.Suit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Game {

    private final Rules type;
    private final List<JassPlayer> players;
    private Suit trump;
    private final Stack<Turn> turns;

    public Game() {
        this.type = new Schieber(new GameCoordinator());
        this.players = new ArrayList<>();
        turns = new Stack<>();
    }

    public JassPlayer joinGame(Player newPlayer) {
        JassPlayer jassPlayer = new JassPlayer(newPlayer, this);
        players.add(jassPlayer);
        return jassPlayer;
    }

    public void startGame() {
        List<Card> initialDeck = type.getInitialDeck();
        handOutCards(initialDeck);
    }

    public void nextTurn() {
        Turn turn = new Turn(this.type, trump);
        this.turns.push(turn);
    }

    public Turn getCurrentTurn() {
        return this.turns.peek();
    }

    public void endCurrentTurn() {
        System.out.println(this.turns.peek().getWinningPlayer());
    }

    Rules getRules() {
        return this.type;
    }

    private void handOutCards(List<Card> initialDeck) {
        type.handOutCards(initialDeck, this.players);
    }
}
