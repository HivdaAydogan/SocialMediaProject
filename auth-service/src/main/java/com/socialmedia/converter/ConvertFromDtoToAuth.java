package com.socialmedia.converter;

import com.socialmedia.dto.request.RegisterRequestDto;
import com.socialmedia.repository.entity.Auth;

public class ConvertFromDtoToAuth {

    public static Auth convertToAuth(RegisterRequestDto dto){
        if(dto == null){
            throw new RuntimeException("Hata");
        }else{
            Auth auth = Auth.builder()
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .email(dto.getEmail())
                    .build();
            return auth;
        }
    }
}
