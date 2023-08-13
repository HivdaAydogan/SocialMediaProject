package com.socialmedia.rabbitmq.consumer;

import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthRegisterConsumer {

    private final UserProfileService userProfileService;

    @RabbitListener(queues = "user-register-queue")
    public void newUserCreate(UserRegisterModel model){
        userProfileService.createUserWithRabbitMq(model);
    }

}
