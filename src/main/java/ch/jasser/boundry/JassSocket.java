package ch.jasser.boundry;

import ch.jasser.control.GameCoordinator;
import ch.jasser.control.actions.ActionResult;

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
import java.util.logging.Logger;

@ServerEndpoint("/jass/{username}/{gameId}")
@ApplicationScoped
public class JassSocket {

    private static final Logger LOG = Logger.getLogger(JassSocket.class.getSimpleName());

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
        System.out.println(String.format("%s connected to Game with Id %s", username, gameId));
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId) {
        sessions.remove(username);
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

        ActionResult act = coordinator.act(gameId, username, jassRequest);
        JassResponses response = act.getResponse();

        for (Map.Entry<String, JassResponse> messageEntry : response.getResponsesPerUser().entrySet()) {
            sendToUser(messageEntry.getKey(), messageEntry.getValue());
        }
    }


    public void sendToUser(String user, Object message) {
        String messageString = this.jsonb.toJson(message);
        sendToUser(messageString, user);
    }

    public void sendToUsers(List<String> players, Object message) {
        String messageString = this.jsonb.toJson(message);
        for (String player : players) {
            sendToUser(player, messageString);
        }
    }

    private void sendToUser(String player, String messageString) {
        if (sessions.containsKey(player)) {
            sessions.get(player).getAsyncRemote().sendObject(messageString, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        } else {
            System.out.println(String.format("Player %s has no active session", player));
        }
    }

    private void broadcast(List<String> users, Object message) {
        String messageString = this.jsonb.toJson(message);
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(messageString, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
