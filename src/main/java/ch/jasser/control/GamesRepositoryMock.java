package ch.jasser.control;

import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Mock
@Priority(1)
@ApplicationScoped
public class GamesRepositoryMock extends GamesRepository {

    private Map<String, Game> games = new HashMap<>();

    public GamesRepositoryMock() {
        super(null);
    }

    @Override
    public void createGame(Game game) {
        this.games.put(game.getGameId(), game);
    }

    @Override
    List<Game> getAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    void addPlayer(String gameId, JassPlayer player) {
        games.get(gameId)
             .getPlayers()
             .add(player);
    }

    @Override
    void addTurn(String gameId, Turn turn) {
        games.get(gameId)
             .getTurns()
             .add(turn);
    }

    @Override
    public Game findById(String uuid) {
        Game game = games.get(uuid);
        return new Game(
                game.getGameId(),
                game.getType(),
                game.getPlayers()
                    .stream()
                    .map(p -> new JassPlayer(p.getName(), p.getHand(), p.getCardsWon()))
                    .collect(Collectors.toList()),
                game.getTurns()
                    .stream()
                    .map(t -> new Turn(t.getCardsOnTable()))
                    .collect(Collectors.toList()),
                game.getTrump(),
                game.getTrumpPlayer(),
                game.getStep(),
                game.getTeams()
        );
    }

    @Override
    public void handOutCard(String gameId, String name, Card card) {
        games.get(gameId)
             .getPlayerByName(name)
             .ifPresent(p -> p.receiveCard(card));
    }

    @Override
    public void removeCardFromPlayer(String gameId, String jassPlayer, Card card) {
        games.get(gameId)
             .getPlayerByName(jassPlayer)
             .ifPresent(p -> p.playCard(card));
    }

    @Override
    public void addCardToTurn(String gameId, String jassPlayer, Card card, int turn) {
        games.get(gameId)
             .getCurrentTurn()
             .getCardsOnTable()
             .add(new PlayedCard(card, jassPlayer));
    }

    @Override
    public void turnToWinningPlayer(String gameId, String winningPlayer) {
        List<Card> cardsOnTable = games.get(gameId)
                                       .getCurrentTurn()
                                       .getCardsOnTable()
                                       .stream()
                                       .map(PlayedCard::getCard)
                                       .collect(Collectors.toList());

        games.get(gameId)
             .getPlayerByName(winningPlayer)
             .ifPresent(p -> p.getCardsWon()
                              .addAll(cardsOnTable));
    }

    @Override
    public void setTrump(String gameId, Suit trump) {
        Game game = games.get(gameId);
        games.put(gameId, new Game(
                game.getGameId(),
                game.getType(),
                game.getPlayers(),
                game.getTurns(),
                trump,
                game.getTrumpPlayer(),
                game.getStep(),
                game.getTeams()
        ));

    }

    @Override
    public void setTeams(String gameId, List<Team> teams) {
        Game game = games.get(gameId);
        games.put(gameId, new Game(
                game.getGameId(),
                game.getType(),
                game.getPlayers(),
                game.getTurns(),
                game.getTrump(),
                game.getTrumpPlayer(),
                game.getStep(),
                teams
        ));
    }

    @Override
    public void addPointsToTeam(String gameId, Team team, int teamPoints) {
        Game game = games.get(gameId);
        List<Team> teams = game.getTeams()
                               .stream()
                               .map(t -> addToTeam(t, team, teamPoints, game.getTurns()
                                                                            .size() - 1))
                               .collect(Collectors.toList());
        games.put(gameId, new Game(
                game.getGameId(),
                game.getType(),
                game.getPlayers(),
                game.getTurns(),
                game.getTrump(),
                game.getTrumpPlayer(),
                game.getStep(),
                teams)
        );

    }

    private Team addToTeam(Team current, Team expected, int teamPoints, int currentTurn) {
        if (current.equals(expected)) {
            return current.addPoints(teamPoints, currentTurn);
        }
        return current;
    }

    @Override
    public void adjustSittingOrder(String gameId, Game game) {
        games.put(gameId, game);
    }
}
