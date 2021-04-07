package es.com.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        for(WebSocketSession webSocketSession : webSocketSessions){
//            webSocketSession.sendMessage(message);
//        }
        super.handleTextMessage(session, message);
        webSocketSessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                LOGGER.error("Error occurred.", e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
