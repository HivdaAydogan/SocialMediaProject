package com.socialmedia.mapper;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.RegisterRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.rabbitmq.model.MailForgotPassModel;
import com.socialmedia.rabbitmq.model.MailSenderModel;
import com.socialmedia.rabbitmq.model.UserForgotPassModel;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.entity.Auth;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth fromAuthRegisterRequestDtoToAuth(final RegisterRequestDto dto);

    RegisterResponseDto fromAuthToAuthRegisterResponseDto(final Auth auth);

    @Mapping(source = "id", target = "authId")
    UserRegisterModel fromAuthToUserRegisterModel(final Auth auth);

    MailSenderModel fromAuthToMailSenderModel(final Auth auth);

    //AuthService --> 1. alternatif
    //UserCreateRequestDto fromRegisterDtoToUserCreateDto(final RegisterRequestDto dto);

    //AuthService --> 2. alternatif
    @Mapping(source = "id", target = "authId")
    UserCreateRequestDto fromRegisterDtoToUserCreateDto(final Auth auth);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auth fromAuthUpdateDtoToAuth(AuthUpdateRequestDto dto, @MappingTarget Auth auth);

    @Mapping(source = "id", target = "authId")
    UserForgotPassModel fromAuthToForgotPassModel(final Auth auth);

    MailForgotPassModel fromAuthToMailForgotPassModel(final Auth auth);

}
