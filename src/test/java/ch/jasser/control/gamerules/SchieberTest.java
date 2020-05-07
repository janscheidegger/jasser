package ch.jasser.control.gamerules;

import ch.jasser.control.GameCoordinator;
import ch.jasser.entity.JassPlayer;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SchieberTest {

    GameCoordinator gameCoordinatorMock = Mockito.mock(GameCoordinator.class);
    Schieber cut = new Schieber();

    @Test
    void shouldReturnASetOf36Cards() {
        List<Card> initialDeck = cut.getInitialDeck();

        assertEquals(36, initialDeck.size());
    }

    @Test
    void shouldHandOutEachPlayerTheSameAmountOfCards() {
        JassPlayer p1 = new JassPlayer("p1");
        JassPlayer p2 = new JassPlayer("p2");
        JassPlayer p3 = new JassPlayer("p3");
        JassPlayer p4 = new JassPlayer("p4");
        List<Card> initialDeck = cut.getInitialDeck();
        List<JassPlayer> players = List.of(p1, p2, p3, p4);
        Game game = mock(Game.class);


        cut.handOutCards(initialDeck, players);

        verify(gameCoordinatorMock, times(9)).handOutCard(game.getGameId(), eq(p1), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(game.getGameId(), eq(p2), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(game.getGameId(), eq(p3), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(game.getGameId(), eq(p4), any(Card.class));
    }
}