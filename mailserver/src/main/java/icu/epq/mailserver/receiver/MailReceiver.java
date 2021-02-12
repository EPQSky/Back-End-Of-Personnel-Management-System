package icu.epq.mailserver.receiver;

import com.rabbitmq.client.Channel;
import icu.epq.minihr.model.Employee;
import icu.epq.minihr.model.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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

    @Autowired
    StringRedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            // redis 中包含该 key，说明该消息已经被消费过
            channel.basicAck(tag, false);
            return;
        }
        // 收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(msg);

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
            javaMailSender.send(msg);
            redisTemplate.opsForHash().put("mail_log", msgId, "remsg");
            channel.basicAck(tag, false);
        } catch (MessagingException e) {
            channel.basicNack(tag, false, true);
            e.printStackTrace();
            LOGGER.error("邮件发送失败！" + e.getMessage());
        }
    }
}
