package properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gaozijie
 * @since 2024-07-11
 */
@Data
@Component
@ConfigurationProperties(prefix = "just-auth")
public class JustAuthProperties {

    @Data
    public static class ClientConfig {
        /**
         * 客户端id
         */
        private String clientId;
        /**
         * 客户端密钥
         */
        private String clientSecret;
    }

    /**
     * 谷歌client配置
     */
    private ClientConfig google;

    /**
     * 脸书client配置
     */
    private ClientConfig facebook;
}
