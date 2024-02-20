package client.websocket;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * @author gaozijie
 * @since 2024-02-06
 */
public class DefaultWebSocketClient extends WebSocketClient {

    public DefaultWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    /**
     * 连接webSocket服务端
     * @param uri uri地址
     * @return webSocket客户端
     */
    public static DefaultWebSocketClient connect(String uri) {
        try {
            URI serverUri = new URI(uri);
            DefaultWebSocketClient webSocketClient = new DefaultWebSocketClient(serverUri);
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.printf("%s --- 链接成功\n", LocalDateTime.now());
    }

    @Override
    public void onMessage(String s) {
        System.out.printf("%s --- message:{%s}\n", LocalDateTime.now(), s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.printf("%s --- 链接关闭\n", LocalDateTime.now());
    }

    @Override
    public void onError(Exception e) {
        System.out.printf("%s --- 链接异常\n", LocalDateTime.now());
    }
}
