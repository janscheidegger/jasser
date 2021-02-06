package ch.jasser;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GameCoordinator;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.actions.ActionResult;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static ch.jasser.boundry.JassRequest.JassRequestBuilder.aJassRequest;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class JassGameIT {

    @Inject
    GamesRepository repository;

    @Inject
    GameCoordinator coordinator;


    @Test
    void firstPlayerShouldChooseTrump() {
        String gameId = UUID.randomUUID()
                            .toString();
        Game game = new TestGameBuilder().withPlayers(
                new TestGameBuilder.JassPlayerBuilder("1").build(),
                new TestGameBuilder.JassPlayerBuilder("2").build(),
                new TestGameBuilder.JassPlayerBuilder("3").build(),
                new TestGameBuilder.JassPlayerBuilder("4").build()
        )
                                         .withMoveAllowed("1")
                                         .withNextStep(GameStep.CHOOSE_TRUMP)
                                         .build(gameId);

        repository.createGame(game);

        ActionResult result = coordinator.act(gameId, "1", JassRequest.JassRequestBuilder.aJassRequest()
                                                                                         .withUsername("1")
                                                                                         .withEvent(EventType.CHOOSE_TRUMP)
                                                                                         .withChosenTrump(Suit.CLUBS)
                                                                                         .build()
        );

        Game gameFromRepository = repository.findById(gameId);

        assertAll(
                () -> assertEquals(Suit.CLUBS, gameFromRepository.getTrump()),
                () -> assertEquals(GameStep.MOVE, result.getNextStep())
        );

    }

    @Test
    void shouldSetPartnerIfChosen() {
        String gameId = UUID.randomUUID()
                            .toString();
        Game game = new TestGameBuilder().withPlayers(
                new TestGameBuilder.JassPlayerBuilder("1").build(),
                new TestGameBuilder.JassPlayerBuilder("2").build(),
                new TestGameBuilder.JassPlayerBuilder("3").build(),
                new TestGameBuilder.JassPlayerBuilder("4").build()
        )
                                         .withMoveAllowed("1")
                                         .withNextStep(GameStep.CHOOSE_TEAMS)
                                         .build(gameId);

        repository.createGame(game);

        ActionResult result = coordinator.act(gameId, "1", JassRequest.JassRequestBuilder.aJassRequest()
                                                                                         .withUsername("1")
                                                                                         .withEvent(EventType.CHOOSE_TEAMS)
                                                                                         .withTeam(Team.of("Team1", "1", "3"))
                                                                                         .withTeam(Team.of("Team2", "2", "4"))
                                                                                         .build()
        );

        Game gameFromRepository = repository.findById(gameId);

        assertAll(
                () -> assertEquals(2, gameFromRepository.getTeams()
                                                        .size()),
                () -> assertEquals(GameStep.HAND_OUT, result.getNextStep())
        );
    }

    @Test
    void shouldSetRandomPartnerIfNotChosenAndAdjustSittingOrder() {
        String gameId = UUID.randomUUID()
                            .toString();
        Game game = new TestGameBuilder().withPlayers(
                new TestGameBuilder.JassPlayerBuilder("1").build(),
                new TestGameBuilder.JassPlayerBuilder("2").build(),
                new TestGameBuilder.JassPlayerBuilder("3").build(),
                new TestGameBuilder.JassPlayerBuilder("4").build()
        )
                                         .withMoveAllowed("1")
                                         .withNextStep(GameStep.CHOOSE_TEAMS)
                                         .build(gameId);

        repository.createGame(game);

        ActionResult result = coordinator.act(gameId, "1", JassRequest.JassRequestBuilder.aJassRequest()
                                                                                         .withUsername("1")
                                                                                         .withEvent(EventType.CHOOSE_TEAMS)
                                                                                         .build()
        );

        Game gameFromRepository = repository.findById(gameId);

        assertAll(
                () -> assertEquals(2, gameFromRepository.getTeams()
                                                        .size()),
                () -> assertEquals(GameStep.HAND_OUT, result.getNextStep()),
                () -> assertEquals(gameFromRepository.getPlayerByName(gameFromRepository.getTeams()
                                                                                        .get(0)
                                                                                        .getPlayers()
                                                                                        .get(0))
                                                     .get(),
                        gameFromRepository.getPlayers()
                                          .get(0)
                )
        );
    }

    @Test
    void everyPlayerShouldPlayLastCardAndRoundShouldEnd() {
        String gameId = UUID.randomUUID()
                            .toString();

        Game game = new TestGameBuilder()
                .withTrump(Suit.CLUBS)
                .withPlayers(
                        new TestGameBuilder.JassPlayerBuilder("1")
                                .withCards(new Card(Rank.JACK, Suit.CLUBS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("2")
                                .withCards(new Card(Rank.QUEEN, Suit.CLUBS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("3")
                                .withCards(new Card(Rank.KING, Suit.CLUBS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("4")
                                .withCards(new Card(Rank.ACE, Suit.CLUBS))
                                .build()
                )
                .withTurns(new Turn())
                .withTeams(Team.of("Team1", "1", "3"), Team.of("Team2", "2", "4"))
                .withMoveAllowed("1")
                .build(gameId);

        repository.createGame(game);
        playCard(gameId, "1", new Card(Rank.JACK, Suit.CLUBS));
        playCard(gameId, "2", new Card(Rank.QUEEN, Suit.CLUBS));
        playCard(gameId, "3", new Card(Rank.KING, Suit.CLUBS));
        ActionResult actionResult = playCard(gameId, "4", new Card(Rank.ACE, Suit.CLUBS));

        Game gameFromRepository = repository.findById(gameId);

        assertAll(
                () -> assertEquals(GameStep.HAND_OUT, actionResult.getNextStep()),
                () -> assertEquals(4, repository.findById(gameId)
                                                .getPlayerByName("4")
                                                .orElseThrow()
                                                .getCardsWon()
                                                .size()),
                () -> assertEquals(0, gameFromRepository.getTeam("Team1")
                                                        .getPoints()),
                () -> assertEquals(38, gameFromRepository.getTeam("Team2")
                                                         .getPoints()),
                () -> assertEquals(1, gameFromRepository.getTeam("Team1")
                                                        .getPointsPerRound()
                                                        .size()),
                () -> assertEquals(1, gameFromRepository.getTeam("Team2")
                                                        .getPointsPerRound()
                                                        .size())
        );
    }

    @Test
    void everyPlayerShouldPlayCardAndTurnShouldEnd() {
        String gameId = UUID.randomUUID()
                            .toString();

        Game game = new TestGameBuilder()
                .withTrump(Suit.CLUBS)
                .withPlayers(
                        new TestGameBuilder.JassPlayerBuilder("1")
                                .withCards(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("2")
                                .withCards(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.QUEEN, Suit.HEARTS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("3")
                                .withCards(new Card(Rank.KING, Suit.CLUBS), new Card(Rank.KING, Suit.HEARTS))
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("4")
                                .withCards(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.ACE, Suit.HEARTS))
                                .build()
                )
                .withTurns(new Turn())
                .withMoveAllowed("1")
                .build(gameId);

        repository.createGame(game);
        playCard(gameId, "1", new Card(Rank.JACK, Suit.CLUBS));
        playCard(gameId, "2", new Card(Rank.QUEEN, Suit.CLUBS));
        playCard(gameId, "3", new Card(Rank.KING, Suit.CLUBS));
        ActionResult actionResult = playCard(gameId, "4", new Card(Rank.ACE, Suit.CLUBS));


        Game gameFromRepository = repository.findById(gameId);
        assertAll(
                () -> assertEquals(0, gameFromRepository.getCurrentTurn().getCardsOnTable().size()),
                () -> assertEquals(GameStep.PRE_TURN, actionResult.getNextStep()),
                () -> assertEquals(1, actionResult.getResponse()
                                                  .getMoveAllowed()
                                                  .size()),
                () -> assertEquals("4", actionResult.getResponse()
                                                    .getMoveAllowed()
                                                    .get(0)
                                                    .getName()),
                () -> assertEquals(4, gameFromRepository
                                                    .getPlayerByName("4")
                                                    .orElseThrow()
                                                    .getCardsWon()
                                                    .size())
        );
    }

    @Test
    void shouldHandOutCards() {
        String gameId = UUID.randomUUID()
                            .toString();

        Game game = new TestGameBuilder()

                .withPlayers(
                        new TestGameBuilder.JassPlayerBuilder("1")
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("2")
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("3")
                                .build(),
                        new TestGameBuilder.JassPlayerBuilder("4")
                                .build()
                )
                .withTrumpPlayer("1")
                .withNextStep(GameStep.HAND_OUT)
                .withMoveAllowed("1", "2", "3", "4")
                .build(gameId);

        repository.createGame(game);

        ActionResult act = coordinator.act(gameId, "1", aJassRequest()
                .withEvent(EventType.HAND_OUT_CARDS)
                .withUsername("1")
                .build()
        );

        assertAll(
                () -> assertEquals(9, act.getResponse()
                                         .getResponsesPerUser()
                                         .get("1")
                                         .get(1)
                                         .getCards()
                                         .size()),
                () -> assertEquals(EventType.CHOOSE_TRUMP, act.getResponse()
                                                              .getResponsesPerUser()
                                                              .get("1")
                                                              .get(0)
                                                              .getEvent()),
                () -> assertEquals(GameStep.CHOOSE_TRUMP, act.getNextStep())

        );


    }

    private ActionResult playCard(String gameId, String username, Card card) {
        return coordinator.act(gameId, username, aJassRequest()
                .withCards(List.of(card))
                .withEvent(EventType.PLAY_CARD)
                .withUsername(null)
                .build()
        );
    }

}
