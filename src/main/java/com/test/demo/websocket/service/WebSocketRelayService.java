package com.test.demo.websocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.websocket.client.PythonWebSocketClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketRelayService {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final Map<String, WebSocketSession> pendingRequests = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile PythonWebSocketClient pythonClient;

    public void setPythonClient(PythonWebSocketClient client) {
        this.pythonClient = client;
    }

    public void registerSession(WebSocketSession session) {
        sessions.add(session);
        System.out.println("[Frontend] Connected: " + session.getId());
    }

    public void unregisterSession(WebSocketSession session) {
        sessions.remove(session);
        pendingRequests.entrySet().removeIf(e -> e.getValue().equals(session));
        System.out.println("[Frontend] Disconnected: " + session.getId());
    }

    /**
     * Forward the "request" string to Python backend.
     * The frontend JSON must have a "request" field.
     */
    public void forwardToPython(String payload, WebSocketSession session) {
        try {
            JsonNode node = mapper.readTree(payload);

            if (!node.has("request")) {
                sendErrorToSession(session, "Missing 'request' field in payload");
                return;
            }

            String request = node.get("request").asText();

            // generate requestId to track response
            String requestId = UUID.randomUUID().toString();
            pendingRequests.put(requestId, session);

            ensurePythonConnected();

            // Wrap as JSON for Python if needed
            // String pythonPayload = mapper.writeValueAsString(Map.of(
            //         "requestId", requestId,
            //         "request", request
            // ));

            String pythonPayload=request;

            pythonClient.send(pythonPayload);

        } catch (Exception e) {
            sendErrorToSession(session, "Failed to forward to Python: " + e.getMessage());
        }
    }

    public void handlePythonMessage(String message) {
        try {
            JsonNode node = mapper.readTree(message);

            String requestId = node.has("requestId") ? node.get("requestId").asText() : null;
            WebSocketSession s = (requestId != null) ? pendingRequests.remove(requestId) : null;

            if (s != null && s.isOpen()) {
                // Send the raw Python response to the correct frontend session
                s.sendMessage(new TextMessage(message));
            } else {
                // If no matching request, broadcast to all sessions
                broadcast(message);
            }
        } catch (Exception e) {
            broadcast("{\"error\":\"Malformed message from Python\"}");
        }
    }

    private void broadcast(String message) {
        sessions.forEach(s -> {
            try {
                if (s.isOpen()) s.sendMessage(new TextMessage(message));
            } catch (IOException ignored) {}
        });
    }

    private void sendErrorToSession(WebSocketSession s, String err) {
        try {
            if (s.isOpen()) s.sendMessage(new TextMessage("{\"error\":\"" + err + "\"}"));
        } catch (IOException ignored) {}
    }

    private void ensurePythonConnected() {
        if (pythonClient == null)
            throw new IllegalStateException("Python client not initialized.");
        if (!pythonClient.isOpen()) {
            pythonClient.reconnect();
        }
    }
}
