package ch.jasser.boundry;

import java.util.HashMap;
import java.util.Map;

public class JassResponses {

    private final Map<String, JassResponse> responsesPerUser = new HashMap<>();

    public JassResponses addResponse(String username, JassResponse message) {
        this.responsesPerUser.put(username, message);
        return this;
    }

    public Map<String, JassResponse> getResponsesPerUser() {
        return responsesPerUser;
    }
}
