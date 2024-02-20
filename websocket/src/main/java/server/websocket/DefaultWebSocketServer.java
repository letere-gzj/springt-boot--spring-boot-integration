package server.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozijie
 * @since 2024-02-06
 */
@ServerEndpoint("/default/{userId}")
@Component
public class DefaultWebSocketServer {

    private static final Map<String, Session> SESSION_CACHE = new ConcurrentHashMap<>(16);

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        SESSION_CACHE.put(userId, session);
        System.out.printf("%s --- 用户:{%s}链接成功，当前在线人数:{%d}\n", LocalDateTime.now(), userId, SESSION_CACHE.size());
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        try(Session removeSession = SESSION_CACHE.remove(userId)) {
            System.out.printf("%s --- 用户:{%s}已下线，当前在线人数:{%d}\n", LocalDateTime.now(), userId, SESSION_CACHE.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.printf("%s --- websocket出现异常:{%s}\n", LocalDateTime.now(), throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("userId") String userId, String message) {
        // websocket接收到消息时调用
        System.out.printf("%s --- message:{%s}\n", LocalDateTime.now(), message);
    }

    /**
     * 向客户端发送消息
     * @param userId 用户Id
     * @param message 消息
     */
    public void sendMessage(String userId, String message){
        Session session = SESSION_CACHE.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
