package ch.jasser.control.actions;

import ch.jasser.boundry.JassRequest;
import ch.jasser.boundry.JassResponse;
import ch.jasser.boundry.JassResponses;
import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GamesRepository;
import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.Team;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Dependent
public class ChoosePartnerAction implements Action {

    private GamesRepository repository;

    public ChoosePartnerAction(GamesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActionResult act(Game game, JassPlayer username, JassRequest message) {
        List<Team> teams;
        if (message.getTeams() == null || message.getTeams().isEmpty() && playerNumberIsDividableBy2(game)) {
            teams = createRandomTeams(game.getPlayers(), 2);
        } else {
            if (areTwoTeamsOfTwo(message.getTeams())) {
                teams = message.getTeams();
            } else {
                return new ActionResult(GameStep.CHOOSE_PARTNER, new JassResponses()
                        .addResponse(username.getName(), JassResponse.JassResponseBuilder.aJassResponse().withEvent(EventType.ERROR).build())
                );
            }
        }
        repository.setTeams(game.getGameId(), teams);

        return new ActionResult(GameStep.HAND_OUT, new JassResponses()
                .addResponse("", JassResponse.JassResponseBuilder.aJassResponse().build()));
    }

    private boolean areTwoTeamsOfTwo(List<Team> teams) {
        for(Team team : teams) {
            if(team.getPlayers().size() != 2) {
                return false;
            }
        }
        return true;
    }

    private boolean playerNumberIsDividableBy2(Game game) {
        return game.getPlayers().size() % 2 == 0;
    }

    @Override
    public EventType getEventType() {
        return EventType.CHOOSE_PARTNER;
    }

    private List<Team> createRandomTeams(List<JassPlayer> players, int teamsize) {
        Collections.shuffle(players);
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamsize; i++) {
            teams.add(Team.of("team"+i));
        }

        for (int i = 0; i < players.size(); i++) {
            teams.set(i % teamsize, teams.get(i % teamsize).addPlayer(players.get(i).getName()));
        }
        return teams;
    }
}
