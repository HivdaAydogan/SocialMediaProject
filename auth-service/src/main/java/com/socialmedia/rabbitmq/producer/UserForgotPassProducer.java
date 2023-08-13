package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.UserForgotPassModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserForgotPassProducer {

    private String exchange = "auth-exchange";
    private String forgotPassBinding = "forgot-pass-binding";

    private final RabbitTemplate rabbitTemplate;

    public void userForgotPassword(UserForgotPassModel userForgotPassModel){
        rabbitTemplate.convertAndSend(exchange, forgotPassBinding, userForgotPassModel);
    }
}
