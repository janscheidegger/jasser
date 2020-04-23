package ch.jasser.boundry.payload;

import ch.jasser.entity.Player;

import java.util.UUID;

public class JoinGamePayload implements Payload {
    private Player player;
    private UUID gameId;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
