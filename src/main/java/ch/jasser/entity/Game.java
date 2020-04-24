package ch.jasser.entity;

import java.util.*;

public class Game {
    private final UUID gameId;
    private Set<Player> players = new HashSet<>();

    public Game() {
        this.gameId = UUID.randomUUID();
    }

    public void addPlayers(String player) {
        this.players.add(new Player(player));
    }

    public UUID getGameId() {
        return gameId;
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId.equals(game.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }
}
