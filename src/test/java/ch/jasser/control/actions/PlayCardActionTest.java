package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PlayCardActionTest {


    private GamesRepository gamesRepository = mock(GamesRepository.class);
    private Rules rules = mock(Rules.class);

    private PlayCardAction cut;

    @BeforeEach
    void beforeEach() {
        cut = new PlayCardAction(gamesRepository, rules);
    }

    @Test
    void shouldPlayACardAndGotoNextPlayer() {
        JassPlayer player1 = new JassPlayer("player1");
        player1.receiveCard(new Card(Rank.ACE, Suit.CLUBS));
        Game game = new Game(
                UUID.randomUUID().toString(),
                GameType.SCHIEBER,
                List.of(player1,
                        new JassPlayer("player2")
                ),
                List.of(new Turn()),
                Suit.CLUBS,
                GameStep.PRE_MOVE
        );
        JassRequest message = new JassRequest();
        message.setEvent(EventType.PLAY_CARD);
        message.setCards(List.of(new Card(Rank.ACE, Suit.CLUBS)));
        ActionResult act = cut.act(game, player1, message);

        assertEquals(1, act.getResponse().getMoveAllowed().size());
        assertEquals("player2", act.getResponse().getMoveAllowed().get(0).getName());
        assertEquals(GameStep.PRE_MOVE, act.getNextStep());
    }

    @Test
    void shouldPlayACardAnFinishTurn() {
        JassPlayer player1 = new JassPlayer("player1");
        JassPlayer player2 = new JassPlayer("player2");
        player1.receiveCard(new Card(Rank.ACE, Suit.CLUBS));

        Turn turn = new Turn(List.of(
                new PlayedCard(
                        new Card(Rank.EIGHT, Suit.CLUBS),
                        player2.getName()
                )
        ));

        Game game = new Game(
                UUID.randomUUID().toString(),
                GameType.SCHIEBER,
                List.of(player1,
                        player2
                ),
                List.of(turn),
                Suit.CLUBS,
                GameStep.MOVE
        );
        JassRequest message = new JassRequest();
        message.setEvent(EventType.PLAY_CARD);
        message.setCards(List.of(new Card(Rank.ACE, Suit.CLUBS)));
        ActionResult act = cut.act(game, player1, message);

        assertEquals(1, act.getResponse().getMoveAllowed().size());
        assertEquals("player2", act.getResponse().getMoveAllowed().get(0).getName());
        assertEquals(GameStep.PRE_MOVE, act.getNextStep());
    }
}
