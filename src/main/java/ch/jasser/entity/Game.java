package ch.jasser.entity;

import ch.jasser.control.JassPlayer;
import ch.jasser.control.gamerules.Rules;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class Game {

    private final UUID gameId;
    private final GameType type;
    private final List<JassPlayer> players;
    private final Stack<Turn> turns;

    public Game() {
        this.gameId = UUID.randomUUID();
        this.type = GameType.SCHIEBER;
        this.players = new ArrayList<>();
        turns = new Stack<>();
    }

    public JassPlayer joinGame(Player newPlayer) {
        JassPlayer jassPlayer = new JassPlayer(newPlayer, this);
        players.add(jassPlayer);
        return jassPlayer;
    }

    public List<JassPlayer> getPlayers() {
        return players;
    }

    public void nextTurn() {
        Turn turn = new Turn();
        this.turns.push(turn);
    }

    @JsonbTransient
    public Turn getCurrentTurn() {
        return this.turns.peek();
    }

    /*public void endCurrentTurn() {
        System.out.println(this.turns.peek().getWinningPlayer());

    }*/


    public UUID getGameId() {
        return gameId;
    }
}
