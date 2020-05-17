package ch.jasser.control;

import ch.jasser.entity.Game;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Collections;

@ApplicationScoped
public class OpenGames {

    private final GamesRepository gamesRepository;

    public OpenGames(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public void addOpenGame(Game game) {
        gamesRepository.createGame(game);
    }

    public Game getGame(String uuid) {
        return gamesRepository.findById(uuid);
    }

    public Collection<Game> getOpenGames() {
        return Collections.unmodifiableCollection(gamesRepository.getAll());
    }

}
