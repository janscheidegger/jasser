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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/jass/{username}/{gameId}")
@ApplicationScoped
public class JassSocket {

    private ActionHandler actionHandler;

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
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, @PathParam("gameId") String gameId, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username, @PathParam("gameId") String gameId) {
        Jsonb jsonb = JsonbBuilder.create();
        JassMessage jassMessage = jsonb.fromJson(message, JassMessage.class);
        Optional<JassMessage> response = actionHandler.handleAction(username, gameId, jassMessage);
        if (response.isPresent()) {
            System.out.println("broadcasting");
            broadcast(jsonb.toJson(response));
        }
    }

    private void sendToUser(String user, String message) {
        sessions.get(user).getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send Message: " + result.getException());
            }
        });
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
