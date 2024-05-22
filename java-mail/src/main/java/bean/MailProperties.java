package bean;

import lombok.Data;

/**
 * 邮件发送自定义配置
 * @author gaozijie
 * @since 2024-05-21
 */
@Data
public abstract class MailProperties {

    /**
     * 邮箱账号
     */
    private String username;

    /**
     * 邮箱授权码
     */
    private String password;

    /**
     * smtp服务器
     */
    private String host;
}
