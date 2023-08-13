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
public class UserRegisterModel implements Serializable {
    Long authId;
    String username;
    String name;
    String surname;
    String email;
    String password;
}
