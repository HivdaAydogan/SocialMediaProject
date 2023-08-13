package com.socialmedia.mapper;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.ToAuthPasswordChangeRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {
    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile fromCreateDtoToUserProfile(UserCreateRequestDto dto);
    UserProfile fromRegisterModelToUserProfile(UserRegisterModel model);

    //BeanMapping, NullValuePropertyMappingStrategy parametresi sayesinde null olarak gönderilen verileri dikkate almaz.
    //Yani update işlemi yapılırken update etmek istemediğiniz property'leri swagger' da sildiğinizde null olacakları için,
    //bunların veri tabanına null olarak kaydedilmemesini sağlar.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile fromUpdateDtoToUserProfile(UserUpdateRequestDto dto, @MappingTarget UserProfile userProfile);

    AuthUpdateRequestDto fromUserProfileToAuthUpdateDto(UserProfile userProfile);

    ToAuthPasswordChangeRequestDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);

}
