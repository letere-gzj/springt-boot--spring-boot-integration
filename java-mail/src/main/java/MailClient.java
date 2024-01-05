
import bean.Mail;
import bean.MailFile;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 邮件工具
 * @author gaozijie
 * @since 2024-01-04
 */
@Component
public class MailClient {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 发送邮件
     * @param mail 邮件内容
     */
    public void sendMail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        this.fillMailContent(mail, mimeMessage);
        javaMailSender.send(mimeMessage);
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
            helper.setTo(mail.getReceiveEmail());
            helper.setFrom(sender, mail.getSenderName());
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
