package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.MailSenderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderProducer {

    private String exchange = "auth-exchange";
    private String mailSenderBinding = "mail-sender-binding";

    private final RabbitTemplate rabbitTemplate;

    public void sendNewMail(MailSenderModel mailSenderModel){
        rabbitTemplate.convertAndSend(exchange,mailSenderBinding,mailSenderModel);
    }


}
