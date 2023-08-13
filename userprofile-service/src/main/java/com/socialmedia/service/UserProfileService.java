package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.exception.UserProfileManagerException;
import com.socialmedia.manager.IAuthManager;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.rabbitmq.model.UserForgotPassModel;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.JwtProvider;
import com.socialmedia.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    public UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public Boolean createUser(UserCreateRequestDto dto){ //dto --> authId, name, surname, email, username
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromCreateDtoToUserProfile(dto));
        return true;
    }

    public Boolean createUserWithRabbitMq(UserRegisterModel model){
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
        return true;
    }

    @Transactional
    public Boolean updateUser(String token, UserUpdateRequestDto dto){
        Optional<Long> authId = jwtProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isPresent()){
            userProfileRepository.save(IUserProfileMapper.INSTANCE.fromUpdateDtoToUserProfile(dto,userProfile.get()));
            //33. satırdan sonra 'userProfile' nesnesi güncel haliyle bulunmaktadır
            AuthUpdateRequestDto authUpdateRequestDto = IUserProfileMapper.INSTANCE.fromUserProfileToAuthUpdateDto(userProfile.get());
            authManager.updateAuth(authUpdateRequestDto);
            return true;
        }
        throw new RuntimeException("Hata");
    }

    public Boolean deleteById(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        userProfile.orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});

        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    public Boolean activateStatus(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        userProfile.orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});

        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean forgotPassword(UserSetPasswordRequestDto dto){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setPassword(dto.getPassword());
        update(userProfile.get());
        return true;
    }

    public Boolean passwordChange(PasswordChangeRequestDto dto){
        Optional<Long> authId = jwtProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw  new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }

        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isPresent()){
            if (passwordEncoder.matches(dto.getOldPassword(), userProfile.get().getPassword())){
                userProfile.get().setPassword(passwordEncoder.encode(dto.getNewPassword()));
                save(userProfile.get());
                //authmanager
                authManager.passwordChange(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(userProfile.get()));
                return true;
            }else {
                throw new UserProfileManagerException(ErrorType.PASSWORD_ERROR);
            }
        }else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPasswordWithRabbitMq(UserForgotPassModel userForgotPassModel){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(userForgotPassModel.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setPassword(userForgotPassModel.getPassword());
        update(userProfile.get());
        return true;
    }


}
