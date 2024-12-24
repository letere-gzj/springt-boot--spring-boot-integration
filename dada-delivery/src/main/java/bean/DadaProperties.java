package bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gaozijie
 * @since 2024-12-23
 */
@Data
@Component
@ConfigurationProperties(prefix = "dada")
public class DadaProperties {
    /**
     * 应用key
     */
    private String appKey;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 商户SourceId
     */
    private String shopSourceId;

    /**
     * 是否使用测试环境
     */
    private Boolean isTestEnv;

    /**
     * 是否开启达达配送
     */
    private Boolean enable;
}
