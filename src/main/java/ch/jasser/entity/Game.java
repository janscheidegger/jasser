package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

@BsonDiscriminator
public class Game {

    private final String gameId;
    private final GameType type;
    private final List<JassPlayer> players;
    private final Stack<Turn> turns = new Stack<>();

    public Game() {
        this.gameId = UUID.randomUUID().toString();
        this.type = GameType.SCHIEBER;
        this.players =  new ArrayList<>();

    }

    @BsonCreator
    public Game(@BsonProperty("gameId") String uuid, @BsonProperty("type") GameType type, @BsonProperty("players") List<JassPlayer> players) {
        this.gameId = uuid;
        this.type = type;
        this.players = players;
    }

    public List<JassPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(String player) {
        this.players.add(new JassPlayer(player));
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

    public String getGameId() {
        return gameId;
    }


}
