package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static ch.jasser.boundry.JassRequest.JassRequestBuilder.aJassRequest;
import static ch.jasser.control.steps.GameStep.CHOOSE_TRUMP;
import static ch.jasser.control.steps.GameStep.PRE_TURN;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ChooseTrumpActionTest {

    private GamesRepository gamesRepository = mock(GamesRepository.class);
    private ChooseTrumpAction cut;

    @BeforeEach
    void beforeEach() {
        cut = new ChooseTrumpAction(gamesRepository);
    }

    @Test
    void shouldChooseTrump() {

        Game game = new Game(
                UUID.randomUUID().toString(),
                GameType.SCHIEBER,
                List.of(
                        new JassPlayer("player1"),
                        new JassPlayer("player2"),
                        new JassPlayer("player3"),
                        new JassPlayer("player4")
                ),
                List.of(),
                null,
                "",
                GameStep.CHOOSE_TRUMP,
                List.of(),
                List.of("player1")
        );

        JassResponses result = cut.act(game, new JassPlayer("player1"), aJassRequest()
                .withEvent(EventType.CHOOSE_TRUMP)
                .withChosenTrump(Suit.HEARTS)
                .build());

        assertAll(
                () -> assertEquals(GameStep.MOVE, result.getResponsesPerUser().get("player1").get(0).getNextStep())
        );
    }

    @Test
    void shouldStayInChooseTrumpOnSchieben() {
        Game game = new Game(
                UUID.randomUUID().toString(),
                GameType.SCHIEBER,
                List.of(
                        new JassPlayer("player1"),
                        new JassPlayer("player2"),
                        new JassPlayer("player3"),
                        new JassPlayer("player4")
                ),
                List.of(),
                null,
                "",
                GameStep.CHOOSE_TRUMP,
                List.of(),
                List.of("player1")
        );

        JassResponses result = cut.act(game, new JassPlayer("player1"), aJassRequest()
                .withEvent(EventType.SCHIEBEN)
                .build());

        assertEquals(CHOOSE_TRUMP, result.getResponsesPerUser().get("player1").get(0).getNextStep());
    }

}
