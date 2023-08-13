package com.socialmedia.rabbitmq.consumer;

import com.socialmedia.rabbitmq.model.MailForgotPassModel;
import com.socialmedia.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailForgotPassConsumer {

    private final MailSenderService mailSenderService;
    @RabbitListener(queues = "forgot-pass-mail-queue")
    public void sendForgotPassword(MailForgotPassModel mailForgotPassModel){
        System.out.println(mailForgotPassModel);
        mailSenderService.sendForgotPassword(mailForgotPassModel);
    }
}
