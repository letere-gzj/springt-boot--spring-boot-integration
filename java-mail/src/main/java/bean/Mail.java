package bean;

import lombok.Data;

import java.util.List;

/**
 * 邮件
 * @author gaozijie
 * @since 2024-01-04
 */
@Data
public class Mail {

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String text;

    /**
     * 内容是否为html文本
     */
    private boolean isHtmlText = false;

    /**
     * 接收邮箱
     */
    private List<String> receiveEmails;

    /**
     * 发送人昵称
     */
    private String senderName;

    /**
     * 附件集合
     */
    List<MailFile> attachFiles;

    /**
     * 内联文件（关联html文本）
     */
    List<MailFile> inlineFiles;
}
