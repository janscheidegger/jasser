package ch.jasser.control.actions;

import ch.jasser.boundry.JassResponses;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class HandOutCardsActionTest {

    GamesRepository repository = mock(GamesRepository.class);

    HandOutCardsAction cut = new HandOutCardsAction(repository);


    @Test
    void shouldHandOutCardsToPlayers() {
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
                "player1",
                GameStep.HAND_OUT,
                List.of(),
                List.of("player1")
        );

        JassResponses act = cut.act(game, new JassPlayer("player1"), null);

        assertAll(
                () -> assertEquals(9, act.getResponsesPerUser().get("player1").get(1).getCards().size()),
                () -> assertEquals(9, act.getResponsesPerUser().get("player2").get(0).getCards().size()),
                () -> assertEquals(9, act.getResponsesPerUser().get("player3").get(0).getCards().size()),
                () -> assertEquals(9, act.getResponsesPerUser().get("player4").get(0).getCards().size())
        );

    }

}
