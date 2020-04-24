package ch.jasser.control;

import ch.jasser.entity.Game;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class OpenGames {

    Map<UUID, Game> games = new ConcurrentHashMap<>();

    public void addOpenGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public Game getGame(UUID uuid) {
        return games.get(uuid);
    }

    public Collection<Game> getOpenGames() {
        return Collections.unmodifiableCollection(games.values());
    }

}
