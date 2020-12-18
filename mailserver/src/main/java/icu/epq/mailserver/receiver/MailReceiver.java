package icu.epq.mailserver.receiver;

import icu.epq.minihr.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author EPQ
 */
@Component
public class MailReceiver {

    public static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    TemplateEngine templateEngine;

    @RabbitListener(queues = "epq.mail.welcome")

    public void handler(Employee employee) {
        LOGGER.info(employee.toString());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setTo(employee.getEmail());
            messageHelper.setFrom(mailProperties.getUsername());
            messageHelper.setSubject("入职欢迎");
            messageHelper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("nickname", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("jobLevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            messageHelper.setText(mail, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("邮件发送失败！" + e.getMessage());
        }
    }
}
