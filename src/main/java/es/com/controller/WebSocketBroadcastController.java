package es.com.controller;

import es.com.dto.response.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBroadcastController {

//    @GetMapping("/stomp-broadcast")
//    public String getWebSocketBroadcast() {
//        return "stomp-broadcast";
//    }
//
//    @GetMapping("/sockjs-broadcast")
//    public String getWebSocketWithSockJsBroadcast() {
//        return "sockjs-broadcast";
//    }

    @MessageMapping("/broadcast")
    @SendTo("/topic/broadcast")
    public ChatMessage send(ChatMessage chatMessage) {
        return new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), "ALL");
    }
}
