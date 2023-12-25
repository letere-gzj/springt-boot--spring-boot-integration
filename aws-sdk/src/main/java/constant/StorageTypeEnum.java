package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gaozijie
 * @since  2023-10-30
 */
@Getter
@AllArgsConstructor
public enum StorageTypeEnum {

    /** minIO */
    MINIO("minio"),

    /** 阿里云 */
    OSS("oss"),

    /** 腾讯云 */
    COS("cos"),

    /** 华为云 */
    OBS("obs"),

    /** 七牛云 */
    KODO("kodo");

    private final String value;
}
