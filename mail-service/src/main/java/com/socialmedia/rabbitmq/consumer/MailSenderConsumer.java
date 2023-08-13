package com.socialmedia.rabbitmq.consumer;

import com.socialmedia.rabbitmq.model.MailSenderModel;
import com.socialmedia.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderConsumer {

    private final MailSenderService mailSenderService;

    @RabbitListener(queues = "mail-sender-queue")
    public void sendRegisterUsersInfo(MailSenderModel mailSenderModel){
        System.out.println(mailSenderModel);
        mailSenderService.sendRegisterUsersInfo(mailSenderModel);

    }

}
