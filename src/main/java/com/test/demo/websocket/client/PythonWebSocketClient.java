package com.test.demo.websocket.client;

import com.test.demo.websocket.service.WebSocketRelayService;

import jakarta.annotation.PreDestroy;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

// import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PythonWebSocketClient extends WebSocketClient {

    private final WebSocketRelayService relayService;
    private final ScheduledExecutorService reconnectExecutor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean reconnecting = new AtomicBoolean(false);

    public PythonWebSocketClient(URI serverUri, WebSocketRelayService relayService) {
        super(serverUri);
        this.relayService = relayService;
        this.relayService.setPythonClient(this);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("[Python] Connected to " + getURI());
    }

    @Override
    public void onMessage(String message) {
        relayService.handlePythonMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("[Python] Connection closed: " + reason);
        scheduleReconnect();
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("[Python] Error: " + ex.getMessage());
        scheduleReconnect();
    }

    private void scheduleReconnect() {
        if (reconnecting.getAndSet(true)) return;

        reconnectExecutor.schedule(() -> {
            try {
                if (!isOpen()) {
                    System.out.println("[Python] Reconnecting...");
                    reconnectBlocking();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                reconnecting.set(false);
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void reconnect() {
        if (!isOpen()) scheduleReconnect();
    }

    @PreDestroy
    public void shutdown() {
        try { closeBlocking(); } catch (InterruptedException ignored) {}
        reconnectExecutor.shutdownNow();
    }

}
