package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

class PlayCardActionTest {


    private GamesRepository gamesRepository = Mockito.mock(GamesRepository.class);

    private PlayCardAction cut;

    @BeforeEach
    void beforeEach() {
        cut = new PlayCardAction(gamesRepository);
    }

    @Test
    void shouldPlayACard() {
        Game game = new Game(
                UUID.randomUUID().toString(),
                GameType.SCHIEBER,
                List.of(new JassPlayer("player1")),
                List.of(),
                Suit.CLUBS,
                GameStep.MOVE
        );
        JassRequest message = new JassRequest();
        message.setUsername("player1");
        message.setEvent(EventType.PLAY_CARD);
        message.setCards(List.of(new Card(Rank.ACE, Suit.CLUBS)));
        ActionResult act = cut.act(game, message);
    }
}
