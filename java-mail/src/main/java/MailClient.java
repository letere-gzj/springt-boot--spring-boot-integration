
import bean.Mail;
import bean.MailFile;
import bean.MailProperties;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 邮件工具
 * @author gaozijie
 * @since 2024-01-04
 */
@Component
public class MailClient {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    /**
     * 发送邮件
     * @param mail 邮件内容
     */
    public void sendMail(Mail mail) {
        this.sendMail(javaMailSender, mail);
    }

    /**
     * 发送邮件
     * @param javaMailSender 邮件发送者
     * @param mail 邮件内容
     */
    public void sendMail(JavaMailSender javaMailSender, Mail mail) {
        if (ObjectUtils.isEmpty(mail.getSenderMailBox())) {
            mail.setSenderMailBox(env.getProperty("spring.mail.username"));
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        this.fillMailContent(mail, mimeMessage);
        javaMailSender.send(mimeMessage);
    }

    /**
     * 构建邮件发送者
     * @param mailProperties 邮件发送配置
     * @return 邮件发送者
     */
    public JavaMailSender buildJavaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setProtocol("smtp");
        return javaMailSender;
    }

    /**
     * 填充邮件内容
     * @param mail 邮件
     * @param mimeMessage 邮件消息
     */
    private void fillMailContent(Mail mail, MimeMessage mimeMessage) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getText(), mail.isHtmlText());
            String[] recMailBoxes = new String[mail.getRecMailBoxes().size()];
            mail.getRecMailBoxes().toArray(recMailBoxes);
            helper.setTo(recMailBoxes);
            helper.setFrom(mail.getSenderMailBox(), mail.getSenderName());
            // 附件
            if (!CollectionUtils.isEmpty(mail.getAttachFiles())) {
                for (MailFile attachFile : mail.getAttachFiles()) {
                    helper.addAttachment(attachFile.getFileName(), attachFile.getDataSource());
                }
            }
            // 内联资源
            if (!CollectionUtils.isEmpty(mail.getInlineFiles())) {
                for (MailFile inlineFile : mail.getInlineFiles()) {
                    helper.addInline(inlineFile.getContentId(), inlineFile.getDataSource());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
