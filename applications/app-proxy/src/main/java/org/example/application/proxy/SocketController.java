package org.example.application.proxy;

import org.example.generic.domain.DomainEvent;
import org.example.generic.GsonEventSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/retrieve/{correlationId}")
public class SocketController {
    private static Map<String, Map<String, Session>> sessions;
    @Autowired
    private GsonEventSerializer serialize;

    public SocketController() {
        if (Objects.isNull(sessions)) {
            sessions = new ConcurrentHashMap<>();
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("correlationId") String correlationId) {
        var map = sessions.getOrDefault(correlationId, new HashMap<>());
        map.put(session.getId(), session);
        sessions.put(correlationId, map);
    }

    @OnClose
    public void onClose(Session session, @PathParam("correlationId") String correlationId) {
        sessions.get(correlationId).remove(session.getId());

    }

    @OnError
    public void onError(Session session, @PathParam("correlationId") String correlationId, Throwable throwable) {
        sessions.get(correlationId).remove(session.getId());
    }

    public void send(String correlationId, DomainEvent event) {

        var message = serialize.serialize(event);
        if (Objects.nonNull(correlationId) && sessions.containsKey(correlationId)) {

            sessions.get(correlationId).values()
                    .forEach(session -> {
                        try {
                            session.getAsyncRemote().sendText(message);
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    });
        }


    }

}