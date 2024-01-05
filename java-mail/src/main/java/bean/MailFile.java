package bean;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 邮件文件
 * @author gaozijie
 * @since 2024-01-04
 */
@Getter
public class MailFile {
    /**
     * 文件名
     */
    private final String fileName;

    /**
     * 文件数据源
     */
    private final DataSource dataSource;

    /**
     * 内联关联名
     */
    private String contentId;

    public MailFile(File file, String contentId) {
        this(file);
        this.contentId = contentId;
    }

    public MailFile(File file) {
        this.fileName = file.getName();
        dataSource = new FileDataSource(file);
    }

    public MailFile(String fileName, byte[] fileData, String contentId) {
        this(fileName, fileData);
        this.contentId = contentId;
    }

    public MailFile(String fileName, byte[] fileData) {
        this.fileName = fileName;
        String contentType = this.getContentType(fileName);
        dataSource = new ByteArrayDataSource(fileData, contentType);
    }

    public MailFile(String fileName, InputStream inputStream, String contentId) {
        this(fileName, inputStream);
        this.contentId = contentId;
    }

    public MailFile(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        String contentType = this.getContentType(fileName);
        try {
            dataSource = new ByteArrayDataSource(inputStream, contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件Mine类型
     * @param fileName 文件名
     * @return mine类型
     */
    private String getContentType(String fileName) {
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        return MediaTypeFactory.getMediaType(suffixName).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
    }
}
