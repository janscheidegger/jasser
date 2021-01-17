package ch.jasser;

import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.GameType;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;

import java.util.ArrayList;
import java.util.List;

public class TestGameBuilder {

    private List<JassPlayer> players = new ArrayList<>();
    private final List<Turn> turns = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();
    private final List<String> moveAllowed = new ArrayList<>();
    private GameStep step = GameStep.MOVE;
    private Suit trump = null;
    private String trumpPlayer;

    TestGameBuilder withPlayers(JassPlayer... jassPlayers) {
        players = List.of(jassPlayers);
        return this;
    }

    TestGameBuilder withTrumpPlayer(String trumpPlayer) {
        this.trumpPlayer = trumpPlayer;
        return this;
    }

    TestGameBuilder withTrump(Suit trump) {
        this.trump = trump;
        return this;
    }

    TestGameBuilder withNextStep(GameStep gameStep) {
        this.step = gameStep;
        return this;
    }

    TestGameBuilder withTurns(Turn... turns) {
        this.turns.add(new TurnBuilder().build());
        return this;
    }

    TestGameBuilder withTeams(Team... teams) {
        this.teams.addAll(List.of(teams));
        return this;
    }

    TestGameBuilder withMoveAllowed(String... players) {
        this.moveAllowed.addAll(List.of(players));
        return this;
    }


    Game build(String uuid) {
        return new Game(uuid,
                GameType.SCHIEBER,
                players,
                turns,
                trump,
                trumpPlayer,
                step,
                teams,
                moveAllowed
                );
    }

    static class TurnBuilder {
        Turn turn = new Turn();
        TurnBuilder withCardsOnTable(PlayedCard... cards) {
            turn = new Turn(List.of(cards));
            return this;
        }

        Turn build() {
            return turn;
        }
    }


    static class JassPlayerBuilder {
        private String name;
        private List<Card> hand = new ArrayList<>();
        private List<Card> cardsWon = new ArrayList<>();
        JassPlayerBuilder(String name) {
            this.name = name;
        }

        JassPlayerBuilder withCards(Card... cards) {
            this.hand = List.of(cards);
            return this;
        }

        JassPlayerBuilder withCardsWon(Card... cards) {
            this.cardsWon = List.of(cards);
            return this;
        }

        JassPlayer build() {
            return new JassPlayer(name, hand, cardsWon);
        }
    }
}
