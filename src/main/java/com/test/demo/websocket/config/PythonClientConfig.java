package com.test.demo.websocket.config;

import com.test.demo.websocket.client.PythonWebSocketClient;
import com.test.demo.websocket.service.WebSocketRelayService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class PythonClientConfig {

    @Bean
    public PythonWebSocketClient pythonWebSocketClient(WebSocketRelayService relayService) throws URISyntaxException {
        URI pythonUri = new URI("ws://13.53.125.55/ws"); // your FastAPI backend
        PythonWebSocketClient client = new PythonWebSocketClient(pythonUri, relayService);
        client.connect(); // non-blocking connect
        return client;
    }
}
