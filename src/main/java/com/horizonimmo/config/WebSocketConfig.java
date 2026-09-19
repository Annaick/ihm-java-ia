package com.horizonimmo.config;

import com.horizonimmo.api.VoiceProxyHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final VoiceProxyHandler voiceProxyHandler;

    public WebSocketConfig(VoiceProxyHandler voiceProxyHandler) {
        this.voiceProxyHandler = voiceProxyHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(voiceProxyHandler, "/ws/voice").setAllowedOrigins("*");
    }
}
