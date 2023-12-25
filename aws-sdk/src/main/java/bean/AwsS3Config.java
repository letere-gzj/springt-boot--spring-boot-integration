package bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaozijie
 * @since  2023-10-27
 */
@Data
@Configuration
public class AwsS3Config {

    @Value("${aws.storageType}")
    private String storageType;

    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;
}
