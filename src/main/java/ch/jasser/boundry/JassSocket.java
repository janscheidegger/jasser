package ch.jasser.boundry;

import ch.jasser.boundry.action.ActionHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/jass/{username}")
@ApplicationScoped
public class JassSocket {

    private ActionHandler actionHandler;

    public JassSocket(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        //broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        Jsonb jsonb = JsonbBuilder.create();
        System.out.println(message);
        JassMessage jassMessage = jsonb.fromJson(message, JassMessage.class);
        System.out.println(jassMessage);
        actionHandler.handleAction(jassMessage.getEvent());
        var response = new JassMessage();
        response.setEvent("getCards");
        broadcast(jsonb.toJson(response));
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            System.out.println("Send Something");
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
