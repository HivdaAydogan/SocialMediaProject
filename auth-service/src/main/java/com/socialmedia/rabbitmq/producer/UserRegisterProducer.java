package com.socialmedia.rabbitmq.producer;

import com.socialmedia.rabbitmq.model.UserRegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterProducer {

    // Değişkenlerin değerleri config sınıfı ile aynı olmak zorundadır.
    private String exchange = "auth-exchange";
    private String userRegisterBinding = "user-register-binding";

    // Bu template, dışarıdan gelen veriyi exchange ve binding değerlerini dikkate alarak kuyruğa eklemeye yarar.
    private final RabbitTemplate rabbitTemplate;

    public void sendNewUser(UserRegisterModel userRegisterModel){
        rabbitTemplate.convertAndSend(exchange,userRegisterBinding,userRegisterModel);
    }
}
