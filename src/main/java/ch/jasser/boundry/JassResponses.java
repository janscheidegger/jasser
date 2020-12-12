package ch.jasser.boundry;

import ch.jasser.entity.JassPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JassResponses {

    private final Map<String, JassResponse> responsesPerUser = new HashMap<>();
    private final List<JassPlayer> moveAllowed = new ArrayList<>();

    public JassResponses addResponse(String username, JassResponse message) {
        this.responsesPerUser.put(username, message);
        return this;
    }

    public JassResponses nextPlayer(JassPlayer jassPlayer) {
        this.moveAllowed.add(jassPlayer);
        return this;
    }

    public List<JassPlayer> getMoveAllowed() {
        return moveAllowed;
    }

    public Map<String, JassResponse> getResponsesPerUser() {
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
