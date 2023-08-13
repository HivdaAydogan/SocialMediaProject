package com.socialmedia.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    // Auth register consumer
    String authRegisterQueue = "user-register-queue";

    @Bean
    Queue authRegisterQueue(){
        return new Queue(authRegisterQueue);
    }

    //Auth forgot password
    private String forgotPassQueue = "forgot-pass-queue";
    @Bean
    Queue forgotPassQueue() {
        return new Queue(forgotPassQueue);
    }

}
