package com.socialmedia.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    // Auth register consumer
    private String mailSenderQueue = "mail-sender-queue";

    @Bean
    Queue mailSenderQueue(){
        return new Queue(mailSenderQueue);
    }

    //Auth forgotPass consumer
    private String forgotPassMailQueue = "forgot-pass-mail-queue";
    @Bean
    Queue forgotPassMailQueue(){
        return new Queue(forgotPassMailQueue);
    }


}
