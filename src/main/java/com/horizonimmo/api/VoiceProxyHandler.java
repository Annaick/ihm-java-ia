package com.horizonimmo.api;

import com.horizonimmo.config.XaiProperties;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Relaie la connexion WebSocket temps reel entre le navigateur et l'agent
 * x.ai (Voice Agent Builder), pour ne jamais exposer XAI_API_KEY au client.
 *
 * L'agent lui-meme (instructions, voix, outils "API Request" vers notre
 * propre API /api/properties et /api/leads) est configure dans le tableau
 * de bord x.ai (console.x.ai/voice/agents) — ce proxy ne fait que
 * transporter les octets, sans logique de function-calling cote Java.
 */
@Component
public class VoiceProxyHandler extends TextWebSocketHandler {

    private static final String SESSION_KEY = "xaiSocket";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private final XaiProperties xaiProperties;

    public VoiceProxyHandler(XaiProperties xaiProperties) {
        this.xaiProperties = xaiProperties;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if (!xaiProperties.isConfigured()) {
            closeQuietly(session, CloseStatus.SERVER_ERROR.withReason("Agent vocal non configure"));
            return;
        }

        URI xaiUri = URI.create("wss://api.x.ai/v1/realtime?agent_id=" + xaiProperties.getAgentId());

        WebSocket.Listener listener = new WebSocket.Listener() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                buffer.append(data);
                if (last) {
                    sendToBrowser(session, buffer.toString());
                    buffer.setLength(0);
                }
                webSocket.request(1);
                return null;
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                closeQuietly(session, CloseStatus.SERVER_ERROR);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                closeQuietly(session, CloseStatus.NORMAL);
                return null;
            }
        };

        WebSocket xaiSocket = HTTP_CLIENT.newWebSocketBuilder()
                .header("Authorization", "Bearer " + xaiProperties.getApiKey())
                .buildAsync(xaiUri, listener)
                .join();

        session.getAttributes().put(SESSION_KEY, xaiSocket);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        WebSocket xaiSocket = (WebSocket) session.getAttributes().get(SESSION_KEY);
        if (xaiSocket != null) {
            xaiSocket.sendText(message.getPayload(), true);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        WebSocket xaiSocket = (WebSocket) session.getAttributes().get(SESSION_KEY);
        if (xaiSocket != null) {
            xaiSocket.sendClose(WebSocket.NORMAL_CLOSURE, "browser closed");
        }
    }

    private void sendToBrowser(WebSocketSession session, String payload) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(payload));
            }
        } catch (Exception e) {
            closeQuietly(session, CloseStatus.SERVER_ERROR);
        }
    }

    private void closeQuietly(WebSocketSession session, CloseStatus status) {
        try {
            if (session.isOpen()) {
                session.close(status);
            }
        } catch (Exception ignored) {
            // session deja fermee
        }
    }
}
