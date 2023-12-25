import bean.AwsS3Config;
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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

/**
 * @author gaozijie
 * @since 2023-10-27
 */
@Component
public class AwsS3Client {

    @Autowired
    private AwsS3Config awsS3Config;

    private S3Client getS3Client() {
        // aws-s3会自动转成桶域名进行访问(https://bucketName.endpoint)，minio不存在桶域名概念，需固定访问路径样式
        boolean isForcePathStyle = Objects.equals(awsS3Config.getStorageType(), StorageTypeEnum.MINIO.getValue());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Config.getAccessKey(), awsS3Config.getSecretKey())))
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(awsS3Config.getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isForcePathStyle)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    private S3Presigner getS3Presigner() {
        // aws-s3会自动转成桶域名进行访问(https://bucketName.endpoint)，minio不存在桶域名概念，需固定访问路径样式
        boolean isForcePathStyle = Objects.equals(awsS3Config.getStorageType(), StorageTypeEnum.MINIO.getValue());
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Config.getAccessKey(), awsS3Config.getSecretKey())))
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(awsS3Config.getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isForcePathStyle)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    /**
     * 获取(下载)文件
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     * @return 文件二进制数据
     */
    public byte[] getFile(String fileName) {
        S3Client s3Client = this.getS3Client();
        GetObjectRequest getObjectReq = this.buildGetObjectReq(fileName, awsS3Config.getBucketName());
        return s3Client.getObjectAsBytes(getObjectReq).asByteArray();
    }

    /**
     * 获取预签名url
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     * @return 预签名url
     */
    public String getPreSignUrl(String fileName) {
        S3Presigner s3Presigner = this.getS3Presigner();
        PutObjectPresignRequest presignRequest = this.buildPutObjectPreSignReq(fileName, awsS3Config.getBucketName());
        return s3Presigner.presignPutObject(presignRequest).url().toString();
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
        PutObjectRequest putObjectReq = this.buildPutObjectReq(fileName, awsS3Config.getBucketName());
        s3Client.putObject(putObjectReq, RequestBody.fromBytes(fileData));
    }

    /**
     * 删除文件
     * @param fileName 文件名（支持路径形式'xx/xx/xxx.png'）
     */
    public void deleteFile(String fileName) {
        S3Client s3Client = this.getS3Client();
        DeleteObjectRequest deleteObjectReq = this.buildDeleteObjectReq(fileName, awsS3Config.getBucketName());
        s3Client.deleteObject(deleteObjectReq);
    }

    /**
     * 构建获取(下载)文件请求
     * @param fileName 文件名
     * @param bucketName 桶名
     * @return 获取(下载)文件请求
     */
    private GetObjectRequest buildGetObjectReq(String fileName, String bucketName) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
    }

    /**
     * 构建上传文件预签名请求
     * @param fileName 文件名
     * @param bucketName 桶名
     * @return 上传文件预签名请求
     */
    private PutObjectPresignRequest buildPutObjectPreSignReq(String fileName, String bucketName) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(60))
                .putObjectRequest(this.buildPutObjectReq(fileName, bucketName))
                .build();
    }

    /**
     * 构建上传文件请求
     * @param fileName 文件名
     * @param bucketName 桶名
     * @return 上传文件请求
     */
    private PutObjectRequest buildPutObjectReq(String fileName, String bucketName) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
    }

    /**
     * 构建删除文件请求
     * @param fileName 文件名
     * @param bucketName 桶名
     * @return 删除文件请求
     */
    private DeleteObjectRequest buildDeleteObjectReq(String fileName, String bucketName) {
        return DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
    }
}
