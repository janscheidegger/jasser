package ch.jasser.boundry;

import ch.jasser.boundry.action.ActionHandler;
import ch.jasser.boundry.action.EventType;
import ch.jasser.boundry.payload.JoinGamePayload;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ServerEndpoint("/jass/{username}/{gameId}")
@ApplicationScoped
public class JassSocket {

    private static final Logger LOG = Logger.getLogger(JassSocket.class.getSimpleName());

    private final ActionHandler actionHandler;
    private final Jsonb jsonb = JsonbBuilder.create();


    public JassSocket(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId) {
        Jsonb jsonb = JsonbBuilder.create();

        sessions.put(username, session);
        System.out.println(String.format("%s connected to Game with Id %s", username, gameId));
        JoinGamePayload payload = new JoinGamePayload();
        payload.setGameId(UUID.fromString(gameId));
        payload.setPlayer(username);
        JassMessage message = new JassMessage();
        message.setEvent(EventType.JOIN_GAME);
        message.setPayloadString(jsonb.toJson(payload));
        actionHandler.handleAction(username, gameId, message);
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
        JassMessage jassMessage = jsonb.fromJson(message, JassMessage.class);
        Optional<JassMessage> response = actionHandler.handleAction(username, gameId, jassMessage);
        if (response.isPresent()) {
            System.out.println("broadcasting");
            sendToUser(username, response.get());
        }
    }

    public void sendToUser(String user, JassMessage message) {
        String messageString = this.jsonb.toJson(message);
        if (sessions.containsKey(user)) {
            sessions.get(user).getAsyncRemote().sendObject(messageString, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send Message: " + result.getException());
                }
            });
        } else {
            System.out.println("User " + user + " has no active session");
        }
    }

    public void sendToUsers(List<String> players, JassMessage message) {
        String messageString = this.jsonb.toJson(message);
        for (String player : players) {
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
    }

    private void broadcast(List<String> users, JassMessage message) {
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
