package icu.epq.minihr.config;

import icu.epq.minihr.model.MailConstants;
import icu.epq.minihr.service.MailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author EPQ
 */
@Configuration
public class RabbitMqConfig {
    public final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    MailSendLogService mailSendLogService;

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                LOGGER.info(msgId + ":消息发送成功");
                mailSendLogService.updateMailSendLogStatus(msgId, 1);
            } else {
                LOGGER.info(msgId + ":消息发送失败");
            }
        });
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            LOGGER.info("消息发送失败");
        });

        return rabbitTemplate;
    }

    @Bean
    Queue mailQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true);
    }

    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

}
