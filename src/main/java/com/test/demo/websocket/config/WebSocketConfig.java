package com.test.demo.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.test.demo.websocket.handler.FrontendWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final FrontendWebSocketHandler frontendHandler;

    public WebSocketConfig(FrontendWebSocketHandler frontendHandler) {
        this.frontendHandler = frontendHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(frontendHandler, "ws/user")
                .setAllowedOrigins("*"); // Allow all for testing
    }
}
