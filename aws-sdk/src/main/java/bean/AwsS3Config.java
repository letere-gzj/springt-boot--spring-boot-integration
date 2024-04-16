package bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaozijie
 * @since  2023-10-27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsS3Config {

    /**
     * 存储类型
     * @see constant.StorageTypeEnum
     */
    private String storageType;

    /**
     * 上传节点
     */
    private String endpoint;

    /**
     * 桶名
     */
    private String bucketName;

    /**
     * 账号（密钥AK）
     */
    private String accessKey;

    /**
     * 密码（密钥SK）
     */
    private String secretKey;
}
