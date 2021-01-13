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
import java.util.Optional;
import java.util.UUID;

@BsonDiscriminator
public class Game {

    private final String gameId;
    private final GameType type;
    private final List<JassPlayer> players;
    private final List<Turn> turns;
    private final Suit trump;
    private final String trumpPlayer;
    private final GameStep step;
    private final List<Team> teams;

    public Game() {
        this.gameId = UUID.randomUUID()
                          .toString();
        this.type = GameType.SCHIEBER;
        this.players = new ArrayList<>();
        this.turns = new LinkedList<>();
        this.trump = null;
        this.step = GameStep.CHOOSE_PARTNER;
        this.teams = new ArrayList<>();
        this.trumpPlayer = null;
    }

    @BsonCreator
    public Game(@BsonProperty("gameId") String uuid,
                @BsonProperty("type") GameType type,
                @BsonProperty("players") List<JassPlayer> players,
                @BsonProperty("turns") List<Turn> turns,
                @BsonProperty("trump") Suit trump,
                @BsonProperty("trumpPlayer") String trumpPlayer,
                @BsonProperty("step") GameStep step,
                @BsonProperty("teams") List<Team> teams) {

        this.gameId = uuid;
        this.type = type;
        this.players = players;
        this.turns = turns;
        this.trump = trump;
        this.trumpPlayer = trumpPlayer;
        this.step = step;
        this.teams = teams;
    }

    public List<JassPlayer> getPlayers() {
        return players;
    }

    public Suit getTrump() {
        return trump;
    }

    public String getTrumpPlayer() {
        return trumpPlayer;
    }

    public Turn nextTurn() {
        Turn turn = new Turn();
        this.turns.add(turn);
        return turn;
    }

    @JsonbTransient
    @BsonIgnore
    public Optional<JassPlayer> getPlayerByName(String name) {
        return players.stream()
                      .filter(p -> p.getName()
                                    .equals(name))
                      .findFirst();
    }

    @JsonbTransient
    @BsonIgnore
    public Turn getCurrentTurn() {
        if (!turns.isEmpty()) {
            return this.turns.get(turns.size() - 1);
        }
        return null;
    }

    @JsonbTransient
    @BsonIgnore
    public Team getTeam(String teamName) {
        return teams.stream()
                    .filter(t -> t.getName()
                                  .equals(teamName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(String.format("Team with name %s not existing", teamName)));
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

    public List<Team> getTeams() {
        return teams;
    }

    public Game adjustSittingOrder(List<Team> teams) {
        List<JassPlayer> jassPlayers = new ArrayList<>();
        for (int i = 0; i < teams.get(0)
                                 .getPlayers()
                                 .size(); i++) {
            for (Team team : teams) {
                jassPlayers.add(getPlayerByName(team.getPlayers()
                                                    .get(i)).orElseThrow(() -> new RuntimeException(
                        "Invalid Player")));
            }
        }
        return new Game(
                gameId,
                type,
                jassPlayers,
                turns,
                trump,
                trumpPlayer,
                step,
                teams
        );
    }
}
