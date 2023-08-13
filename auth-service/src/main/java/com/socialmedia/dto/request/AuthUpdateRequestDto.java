package com.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUpdateRequestDto {
    private Long authId;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
}
