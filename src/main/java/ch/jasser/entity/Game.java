package ch.jasser.entity;

import ch.jasser.control.steps.GameStep;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@BsonDiscriminator
public class Game {

    private final String gameId;
    private final GameType type;
    private final List<JassPlayer> players;
    private final List<Turn> turns;
    private Suit trump;
    private GameStep step;

    public Game() {
        this.gameId = UUID.randomUUID().toString();
        this.type = GameType.SCHIEBER;
        this.players = new ArrayList<>();
        this.turns = new LinkedList<>();
        this.trump = null;
        this.step = null;

    }

    @BsonCreator
    public Game(@BsonProperty("gameId") String uuid,
                @BsonProperty("type") GameType type,
                @BsonProperty("players") List<JassPlayer> players,
                @BsonProperty("turns") List<Turn> turns,
                @BsonProperty("trump") Suit trump,
                @BsonProperty("step") GameStep step) {
        this.gameId = uuid;
        this.type = type;
        this.players = players;
        this.turns = turns;
        this.trump = trump;
        this.step = step;
    }

    public List<JassPlayer> getPlayers() {
        return players;
    }

    public Suit getTrump() {
        return trump;
    }

    public void setTrump(Suit trump) {
        this.trump = trump;
    }

    public Turn nextTurn() {
        Turn turn = new Turn();
        this.turns.add(turn);
        return turn;
    }

    @JsonbTransient
    @BsonIgnore
    public Turn getCurrentTurn() {
        if (!turns.isEmpty()) {
            return this.turns.get(turns.size() - 1);
        }
        return null;
    }

    public GameType getType() {
        return this.type;
    }

    public String getGameId() {
        return gameId;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public GameStep getStep() {
        return step;
    }
}
