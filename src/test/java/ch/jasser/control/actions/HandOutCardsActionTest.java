package ch.jasser.control.actions;

import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HandOutCardsActionTest {

    HandOutCardsAction cut = new HandOutCardsAction();


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
                GameStep.HAND_OUT,
                List.of()
        );

        ActionResult act = cut.act(game, new JassPlayer("1"), null);

        assertAll(
                () -> assertEquals(9, act.getResponse().getResponsesPerUser().get("player1").getHand().size()),
                () -> assertEquals(9, act.getResponse().getResponsesPerUser().get("player2").getHand().size()),
                () -> assertEquals(9, act.getResponse().getResponsesPerUser().get("player3").getHand().size()),
                () -> assertEquals(9, act.getResponse().getResponsesPerUser().get("player4").getHand().size())
        );

    }

}
