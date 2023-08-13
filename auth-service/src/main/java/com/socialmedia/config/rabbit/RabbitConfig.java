package com.socialmedia.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    /**
     * Bu rabbitmq configuration sınıfı producer veya consumer işlemleri için gerekli alt yapıyı oluşturmayı sağlar.
     * Producer işlemi(bu serviste bir kuyruk üret ve verileri bu kuyruk üzerinden gönder) yapılacağında exchange,
     * queue ve bindingKey gibi değişkenlere itiyaç duyulur.
     * Ancak Consumer işlemi yapılacağı zaman yalnızca tüketilecek olan queue oluşturularak kuyruktan gelen veri deserialize edilir.
     */

    private String exchange = "auth-exchange"; // tektir, yani her producer işlemi için birden çok oluşturmaya gerek yok
    @Bean
    DirectExchange authExchange(){
        return new DirectExchange(exchange);
    }

    // User register producer
    private String userRegisterQueue = "user-register-queue";  // her producer işlemi için yeniden bir değişken oluşturulmalıdır
    private String userRegisterBinding = "user-register-binding"; // her producer işlemi için yeniden bir değişken oluşturulmalıdır

    @Bean
    Queue userRegisterQueue(){
        return new Queue(userRegisterQueue);
    }

    @Bean
    public Binding userRegisterBinding(final Queue userRegisterQueue, final DirectExchange authExchange){
        return BindingBuilder
                .bind(userRegisterQueue)
                .to(authExchange)
                .with(userRegisterBinding);
    }


    // Mail sender producer
    private String mailSenderQueue = "mail-sender-queue";
    private String mailSenderBinding = "mail-sender-binding";

    @Bean
    Queue mailSenderQueue(){
        return new Queue(mailSenderQueue);
    }

    @Bean
    public Binding mailSenderBinding(final Queue mailSenderQueue, final DirectExchange authExchange){
        return BindingBuilder
                .bind(mailSenderQueue)
                .to(authExchange)
                .with(mailSenderBinding);
    }

    //User forgot password
    private String forgotPassQueue = "forgot-pass-queue";
    @Bean
    Queue forgotPassQueue(){
        return new Queue(forgotPassQueue);
    }

    private String forgotPassBinding = "forgot-pass-binding";

    @Bean
    public Binding forgotPassBinding(final Queue forgotPassQueue, final DirectExchange authExchange){
        return BindingBuilder.bind(forgotPassQueue).to(authExchange).with(forgotPassBinding);
    }

    //Mail forgot password
    private String forgotPassMailQueue = "forgot-pass-mail-queue";
    @Bean
    Queue forgotPassMailQueue(){
        return new Queue(forgotPassMailQueue);
    }

    private String forgotPassMailBinding = "forgot-pass-mail-binding";

    @Bean
    public Binding forgotPassMailBinding(final Queue forgotPassMailQueue, final DirectExchange authExchange){
        return BindingBuilder.bind(forgotPassMailQueue).to(authExchange).with(forgotPassMailBinding);
    }


}
