package client.controller;

import client.websocket.DefaultWebSocketClient;
import jakarta.servlet.http.HttpServletRequest;
import org.java_websocket.client.WebSocketClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozijie
 * @since 2024-02-20
 */
@RestController
@RequestMapping("/websocket/client")
public class DefaultWebSocketController {
    /**
     * webSocket缓存
     */
    private final Map<String, WebSocketClient> cache = new ConcurrentHashMap<>(16);

    @GetMapping("/connect")
    public void connect(String uri, HttpServletRequest request) {
        // 唯一标识，例如获取当前登录的用户id之类，这里用SessionId表示
        String sessionId = request.getSession().getId();
        DefaultWebSocketClient socketClient = DefaultWebSocketClient.connect(uri);
        cache.put(sessionId, socketClient);
    }

    @GetMapping("/close")
    public void close(HttpServletRequest request) {
        // 唯一标识，例如获取当前登录的用户id之类，这里用SessionId表示
        String sessionId = request.getSession().getId();
        WebSocketClient webSocketClient = cache.remove(sessionId);
        webSocketClient.close();
    }

    @GetMapping("/sendMsg")
    public void sendMsg(String msg, HttpServletRequest request) {
        // 唯一标识，例如获取当前登录的用户id之类，这里用SessionId表示
        String sessionId = request.getSession().getId();
        WebSocketClient webSocketClient = cache.get(sessionId);
        webSocketClient.send(msg);
    }
}
