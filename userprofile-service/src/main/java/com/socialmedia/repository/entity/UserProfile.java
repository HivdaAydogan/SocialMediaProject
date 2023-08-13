package com.socialmedia.repository.entity;

import com.socialmedia.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Document
public class UserProfile extends BaseEntity{

    @Id
    private String id;
    private Long authId;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private String info;
    private String address;
    @Builder.Default
    private EStatus status = EStatus.PENDING;
}
