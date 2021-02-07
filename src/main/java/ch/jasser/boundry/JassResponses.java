package ch.jasser.boundry;

import ch.jasser.control.steps.GameStep;
import ch.jasser.entity.JassPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class  JassResponses {

    private final Map<String, List<JassResponse>> responsesPerUser = new HashMap<>();
    private final List<JassPlayer> moveAllowed = new ArrayList<>();
    private final GameStep nextStep;

    public JassResponses(GameStep nextStep) {
        this.nextStep = nextStep;
    }

    public JassResponses addResponse(String username, JassResponse message) {
        message.setNextStep(this.nextStep);
        this.responsesPerUser.computeIfAbsent(username, r -> new ArrayList<>()).add(message);
        return this;
    }

    public JassResponses addResponse(List<String> players, JassResponse response) {
        for(String player : players) {
            addResponse(player, response);
        }
        return this;
    }

    public JassResponses nextPlayer(List<JassPlayer> jassPlayer) {
        this.moveAllowed.addAll(jassPlayer);
        return this;
    }

    public List<JassPlayer> getMoveAllowed() {
        return moveAllowed;
    }

    public Map<String, List<JassResponse>> getResponsesPerUser() {
        return responsesPerUser;
    }

    @Override
    public String toString() {
        return "JassResponses{" +
                "responsesPerUser=" + responsesPerUser +
                ", moveAllowed=" + moveAllowed +
                '}';
    }

}
