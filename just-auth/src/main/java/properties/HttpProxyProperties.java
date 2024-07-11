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
@ConfigurationProperties(prefix = "http.proxy")
public class HttpProxyProperties {

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;
}
