package ch.jasser.boundry;

import ch.jasser.boundry.action.EventType;
import ch.jasser.control.GameCoordinator;
import ch.jasser.entity.JassPlayer;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ch.jasser.boundry.JassResponse.JassResponseBuilder.aJassResponse;

@ServerEndpoint("/jass/{username}/{gameId}")
@ApplicationScoped
public class JassSocket {


    private final Jsonb jsonb = JsonbBuilder.create();

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    private GameCoordinator coordinator;

    public JassSocket(GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId) {

        sessions.put(username, session);
        coordinator.joinGame(gameId, username);
        List<String> jassPlayers = getOtherPlayers(username, gameId);
        sendToUsers(jassPlayers, aJassResponse()
                .withEvent(EventType.PLAYER_JOINED)
                .withUsername(username)
                .build());
        System.out.printf("%s connected to Game with Id %s%n", username, gameId);
    }

    private List<String> getOtherPlayers(String username, String gameId) {
        return coordinator.getPlayers(gameId)
                          .stream()
                          .filter(
                                  n -> !n.getName()
                                         .equals(username)
                          )
                          .map(JassPlayer::getName)
                          .collect(Collectors.toList());
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId) {
        sessions.remove(username);
        sendToUsers(getOtherPlayers(username, gameId),
                aJassResponse().withEvent(EventType.PLAYER_LEFT)
                               .withUsername(username)
                               .build());
        System.out.println("Session closed");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId, Throwable throwable) throws Throwable {
        sessions.remove(username);
        System.err.println(throwable);
        throw throwable;
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("gameId") String gameId) {

        Jsonb jsonb = JsonbBuilder.create();
        JassRequest jassRequest = jsonb.fromJson(message, JassRequest.class);

        JassResponses response = coordinator.act(gameId, username, jassRequest);

        for (Map.Entry<String, List<JassResponse>> messageEntry : response.getResponsesPerUser()
                                                                          .entrySet()) {
            sendToUser(messageEntry.getKey(), messageEntry.getValue());
        }

        if (response.getResponsesPerUser()
                    .containsKey("")) {
            for (JassPlayer player : coordinator.getGameState(gameId)
                                                .getPlayers()) {
                sendToUser(player.getName(), response.getResponsesPerUser()
                                                     .get(""));
            }
        }
    }


    public void sendToUser(String user, List<JassResponse> messages) {
        for (JassResponse message : messages) {
            String messageString = this.jsonb.toJson(message);
            sendToUser(user, messageString);
        }
    }

    public void sendToUsers(List<String> players, JassResponse message) {
        String messageString = this.jsonb.toJson(message);
        for (String player : players) {
            sendToUser(player, messageString);
        }
    }

    private void sendToUser(String player, String messageString) {
        if (sessions.containsKey(player)) {
            sessions.get(player)
                    .getAsyncRemote()
                    .sendObject(messageString, result -> {
                        if (result.getException() != null) {
                            System.out.println("Unable to send message: " + result.getException());
                        }
                    });
        } else {
            System.out.println(String.format("Player %s has no active session", player));
        }
    }
}
