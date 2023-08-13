package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.MailForgotPassModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailForgotPassProducer {

    private String exchange = "auth-exchange";

    private String forgotPassMailBinding = "forgot-pass-mail-binding";

    private final RabbitTemplate rabbitTemplate;

    public void forgotPassSendMail(MailForgotPassModel mailForgotPassModel){
        rabbitTemplate.convertAndSend(exchange, forgotPassMailBinding, mailForgotPassModel);
    }
}
