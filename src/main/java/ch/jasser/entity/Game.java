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
        this.turns = new Stack<>();
    }

    public Game(UUID uuid, GameType type) {
        this.gameId = uuid;
        this.type = type;
        this.players = new ArrayList<>();
        this.turns = new Stack<>();
    }

    public List<JassPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(String name) {
        this.players.add(new JassPlayer(new Player(name), this));
    }

    public void nextTurn() {
        Turn turn = new Turn();
        this.turns.push(turn);
    }

    @JsonbTransient
    public Turn getCurrentTurn() {
        return this.turns.peek();
    }

    public GameType getType() {
        return this.type;
    }

    public UUID getGameId() {
        return gameId;
    }


}
