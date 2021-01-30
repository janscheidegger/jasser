package ch.jasser.boundry;

import ch.jasser.entity.JassPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  JassResponses {

    private final Map<String, List<JassResponse>> responsesPerUser = new HashMap<>();
    private final List<JassPlayer> moveAllowed = new ArrayList<>();

    public JassResponses addResponse(String username, JassResponse message) {
        this.responsesPerUser.computeIfAbsent(username, r -> new ArrayList<>()).add(message);
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
