package com.test.demo.websocket.handler;

import com.test.demo.websocket.service.WebSocketRelayService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class FrontendWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketRelayService relayService;

    public FrontendWebSocketHandler(WebSocketRelayService relayService) {
        this.relayService = relayService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("[Frontend] Connected: " + session.getId());
        relayService.registerSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("[Frontend] Received: " + message.getPayload());
        relayService.forwardToPython(message.getPayload(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        relayService.unregisterSession(session);
    }
}
