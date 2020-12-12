package ch.jasser;

import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GameCoordinator;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.actions.ActionResult;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;
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
    void everyPlayerShouldPlayLastCardAndRoundShouldEnd() {
        String gameId = UUID.randomUUID().toString();

        Game game = new TestGameBuilder()
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
                .build(gameId);

        repository.createGame(game);
        System.out.println(playCard(gameId, "1", new Card(Rank.JACK, Suit.CLUBS)));
        System.out.println(playCard(gameId, "2", new Card(Rank.QUEEN, Suit.CLUBS)));
        System.out.println(playCard(gameId, "3", new Card(Rank.KING, Suit.CLUBS)));
        ActionResult actionResult = playCard(gameId, "4", new Card(Rank.ACE, Suit.CLUBS));

        assertAll(
                () -> assertEquals(GameStep.PRE_ROUND, actionResult.getNextStep())
        );
    }

    @Test
    void everyPlayerShouldPlayCardAndTurnShouldEnd() {
        String gameId = UUID.randomUUID().toString();

        Game game = new TestGameBuilder()
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
                .build(gameId);

        repository.createGame(game);
        playCard(gameId, "1", new Card(Rank.JACK, Suit.CLUBS));
        playCard(gameId, "2", new Card(Rank.QUEEN, Suit.CLUBS));
        playCard(gameId, "3", new Card(Rank.KING, Suit.CLUBS));
        ActionResult actionResult = playCard(gameId, "4", new Card(Rank.ACE, Suit.CLUBS));

        assertAll(
                () -> assertEquals(GameStep.PRE_TURN, actionResult.getNextStep()),
                () -> assertEquals(1, actionResult.getResponse().getMoveAllowed().size()),
                () -> assertEquals("4", actionResult.getResponse().getMoveAllowed().get(0).getName())
        );
    }

    @Test
    void shouldHandOutCards() {
        String gameId = UUID.randomUUID().toString();

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
                .withNextStep(GameStep.PRE_ROUND)
                .build(gameId);

        repository.createGame(game);

        ActionResult act = coordinator.act(gameId, "1", aJassRequest()
                .withEvent(EventType.HAND_OUT_CARDS)
                .withUsername("1")
                .build()
        );

        assertAll(
                () -> assertEquals(9, act.getResponse().getResponsesPerUser().get("1").getHand().size()),
                () -> assertEquals(EventType.RECEIVE_CARD, act.getResponse().getResponsesPerUser().get("1").getEvent()),
                () -> assertEquals(GameStep.PRE_MOVE, act.getNextStep())

        );


    }

    private ActionResult playCard(String gameId, String username, Card card) {
        return coordinator.act(gameId, username, aJassRequest()
                .withCards(List.of(card))
                .withEvent(EventType.PLAY_CARD)
                .withUsername(username).build()
        );
    }

}
