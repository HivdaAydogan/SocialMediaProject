package com.socialmedia.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MailSenderModel implements Serializable {

    String username;
    String email;
    String activationCode;
    String password;

}
