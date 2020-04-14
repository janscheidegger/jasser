package ch.jasser.control.gamerules;

import ch.jasser.control.GameCoordinator;
import ch.jasser.control.JassPlayer;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.entity.Card;
import ch.jasser.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SchieberTest {

    GameCoordinator gameCoordinatorMock = Mockito.mock(GameCoordinator.class);
    Schieber cut = new Schieber(gameCoordinatorMock);

    @Test
    void shouldReturnASetOf36Cards() {
        List<Card> initialDeck = cut.getInitialDeck();

        assertEquals(36, initialDeck.size());
    }

    @Test
    void shouldHandOutEachPlayerTheSameAmountOfCards() {
        JassPlayer p1 = new JassPlayer(new Player("p1"), null);
        JassPlayer p2 = new JassPlayer(new Player("p2"), null);
        JassPlayer p3 = new JassPlayer(new Player("p3"), null);
        JassPlayer p4 = new JassPlayer(new Player("p4"), null);
        List<Card> initialDeck = cut.getInitialDeck();
        List<JassPlayer> players = List.of(p1, p2, p3, p4);


        cut.handOutCards(initialDeck, players);

        verify(gameCoordinatorMock, times(9)).handOutCard(eq(p1), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(eq(p2), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(eq(p3), any(Card.class));
        verify(gameCoordinatorMock, times(9)).handOutCard(eq(p4), any(Card.class));
    }
}