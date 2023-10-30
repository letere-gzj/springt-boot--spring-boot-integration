import bean.AwsS3Configuration;
import constant.StorageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

/**
 * @author gaozijie
 * @date 2023-10-27
 */
@Component
public class AwsS3Client {

    @Autowired
    private AwsS3Configuration awsS3Configuration;

    private S3Client getS3Client() {
        // aws-s3会自动转成桶域名进行访问(https://bucketName.endpoint)，minio不存在桶域名概念，需固定访问路径样式
        boolean isForcePathStyle = Objects.equals(awsS3Configuration.getStorageType(), StorageTypeEnum.MINIO.getValue());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Configuration.getAccessKey(), awsS3Configuration.getSecretKey())))
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(awsS3Configuration.getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isForcePathStyle)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    private S3Presigner getS3Presigner() {
        // aws-s3会自动转成桶域名进行访问(https://bucketName.endpoint)，minio不存在桶域名概念，需固定访问路径样式
        boolean isForcePathStyle = Objects.equals(awsS3Configuration.getStorageType(), StorageTypeEnum.MINIO.getValue());
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Configuration.getAccessKey(), awsS3Configuration.getSecretKey())))
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(awsS3Configuration.getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isForcePathStyle)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    /**
     * 获取预签名url
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     * @return 预签名url
     */
    public String getPreSignUrl(String fileName) {
        S3Presigner s3Presigner = this.getS3Presigner();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(60))
                .putObjectRequest(PutObjectRequest.builder()
                        .bucket(awsS3Configuration.getBucketName())
                        .key(fileName)
                        .build())
                .build();
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    /**
     * 上传文件（通过预签名url）
     * @param preSignUrl 预签名url
     * @param fileData 文件二进制数据
     */
    public void uploadFileByPreSignUrl(String preSignUrl, byte[] fileData) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(preSignUrl))
                .PUT(HttpRequest.BodyPublishers.ofByteArray(fileData))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (!Objects.equals(response.statusCode(), 200)) {
                throw new RuntimeException("预签名url上传失败！失败内容为：" + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     * @param fileData 文件二进制数据
     */
    public void uploadFile(String fileName, byte[] fileData) {
        S3Client s3Client = this.getS3Client();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsS3Configuration.getBucketName())
                .key(fileName)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(fileData));
    }

    /**
     * 删除文件
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     */
    public void deleteFile(String fileName) {
        S3Client s3Client = this.getS3Client();
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(awsS3Configuration.getBucketName())
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
