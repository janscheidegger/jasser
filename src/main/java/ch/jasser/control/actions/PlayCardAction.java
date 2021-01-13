package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponse;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;

import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.Optional;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@Dependent
public class PlayCardAction implements Action {

    private final GamesRepository repository;
    private final Rules rules;

    public PlayCardAction(GamesRepository repository, Rules rules) {
        this.repository = repository;
        this.rules = rules;
    }

    @Override
    public ActionResult act(Game game, JassPlayer player, JassRequest message) {
        Card card = message.getCards()
                           .get(0);
        if (isAllowedToPlayCard(game, player, card)) {
            playCard(game, player, card);
            GameStep nextStep = getNextStep(game);
            JassPlayer nextPlayer;

            switch (nextStep) {
                case PRE_TURN: {
                    PlayedCard winningCard = rules.getWinningCard(game.getCurrentTurn()
                                                                      .getCardsOnTable(), game.getCurrentTurn()
                                                                                              .getPlayedSuit(), game.getTrump());
                    nextPlayer = game.getPlayerByName(winningCard.getPlayer())
                                     .orElseThrow(RuntimeException::new);
                    repository.turnToWinningPlayer(game.getGameId(), nextPlayer.getName());

                    for (Team team : game.getTeams()) {
                        int teamPoints = team.getPlayers()
                                             .stream()
                                             .map(game::getPlayerByName)
                                             .flatMap(Optional::stream)
                                             .map(JassPlayer::getCardsWon)
                                             .map(cards -> rules.countPoints(cards, game.getTrump()))
                                             .mapToInt(Integer::valueOf)
                                             .sum();
                        team.addPoints(teamPoints, game.getTurns()
                                                       .size() - 1);
                        repository.addPointsToTeam(game.getGameId(), team, teamPoints);
                    }


                    break;
                }
                case HAND_OUT: {
                    PlayedCard winningCard = rules.getWinningCard(game.getCurrentTurn()
                                                                      .getCardsOnTable(), game.getCurrentTurn()
                                                                                              .getPlayedSuit(), game.getTrump());
                    repository.turnToWinningPlayer(game.getGameId(), winningCard.getPlayer());

                    /* POST ACTION??? */
                    for (Team team : game.getTeams()) {
                        int teamPoints = team.getPlayers()
                                             .stream()
                                             .map(game::getPlayerByName)
                                             .flatMap(Optional::stream)
                                             .map(JassPlayer::getCardsWon)
                                             .map(cards -> rules.countPoints(cards, game.getTrump()))
                                             .mapToInt(Integer::valueOf)
                                             .sum();
                        team.addPoints(teamPoints, game.getTurns()
                                                       .size() - 1);
                        repository.addPointsToTeam(game.getGameId(), team, teamPoints);
                    }

                    /* POST ACTION??? */

                    nextPlayer = null; // Everyone is allowed to trigger hand out of cards
                    break;
                }
                case PRE_MOVE:
                    nextPlayer = getNextPlayer(player, game.getPlayers());
                    break;
                default:
                    throw new RuntimeException("Not yet implemented");
            }

            JassResponse responseForPlayer = aJassResponse().withEvent(EventType.CARD_PLAYED)
                                                            .withHand(player.getHand())
                                                            .withCards(List.of(card))
                                                            .build();
            JassResponse responseForAll = aJassResponse().withEvent(EventType.CARD_PLAYED)
                                                         .withCards(List.of(card))
                                                         .build();

            JassResponses jassResponses = new JassResponses()
                    .addResponse(player.getName(), responseForPlayer)
                    .addResponse("", responseForAll)
                    .nextPlayer(nextPlayer);
            return new ActionResult(nextStep, jassResponses);
        } else {
            JassResponse response = aJassResponse().withEvent(EventType.ERROR)
                                                   .withMessage(String.format("Not allowed to play card: (%s)", card))
                                                   .withUsername(player.getName())
                                                   .build();
            return new ActionResult(GameStep.MOVE, new JassResponses().addResponse(player.getName(), response));
        }
    }

    private GameStep getNextStep(Game game) {
        if (game.getCurrentTurn()
                .getCardsOnTable()
                .size() == game.getPlayers()
                               .size() &&
                noMoreCardsOnHands(game.getPlayers())) {
            return GameStep.HAND_OUT;
        }
        if (game.getCurrentTurn()
                .getCardsOnTable()
                .size() == game.getPlayers()
                               .size()) {
            return GameStep.PRE_TURN;
        }
        return GameStep.PRE_MOVE;
    }

    private boolean noMoreCardsOnHands(List<JassPlayer> players) {
        return players.stream()
                      .map(JassPlayer::getHand)
                      .filter(h -> !h.isEmpty())
                      .findFirst()
                      .isEmpty();
    }

    private JassPlayer getNextPlayer(JassPlayer currentPlayer, List<JassPlayer> players) {
        return players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    private void playCard(Game game, JassPlayer player, Card card) {
        repository.addCardToTurn(game.getGameId(), player.getName(), card, game.getTurns()
                                                                               .size());
        repository.removeCardFromPlayer(game.getGameId(), player.getName(), card);
        player.playCard(card);
    }

    @Override
    public EventType getEventType() {
        return EventType.PLAY_CARD;
    }

    private boolean isAllowedToPlayCard(Game game, JassPlayer jassPlayer, Card card) {
        if (!jassPlayer.getHand()
                       .contains(card)) {
            return false;
        }
        Turn currentTurn = game.getCurrentTurn();
        return currentTurn.getCardsOnTable()
                          .size() == 0
                || currentTurn.getPlayedSuit()
                              .equals(card.getSuit())
                || (card.getSuit()
                        .equals(game.getTrump()));
    }
}
