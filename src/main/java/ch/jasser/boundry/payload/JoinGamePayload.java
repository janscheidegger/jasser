package ch.jasser.boundry.payload;

import ch.jasser.entity.Player;

import java.util.UUID;

public class JoinGamePayload implements Payload {
    private String player;
    private UUID gameId;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
