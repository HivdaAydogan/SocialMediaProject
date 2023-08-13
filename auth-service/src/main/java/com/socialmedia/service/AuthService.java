package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.manager.IUserProfileManager;
import com.socialmedia.mapper.IAuthMapper;
import com.socialmedia.rabbitmq.model.MailForgotPassModel;
import com.socialmedia.rabbitmq.producer.MailForgotPassProducer;
import com.socialmedia.rabbitmq.producer.MailSenderProducer;
import com.socialmedia.rabbitmq.producer.UserForgotPassProducer;
import com.socialmedia.rabbitmq.producer.UserRegisterProducer;
import com.socialmedia.repository.IAuthRepository;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.CodeGenerator;
import com.socialmedia.utility.JwtProvider;
import com.socialmedia.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userProfileManager;
    private final UserRegisterProducer userRegisterProducer;
    private final MailSenderProducer mailSenderProducer;
    private final UserForgotPassProducer userForgotPassProducer;
    private final PasswordEncoder passwordEncoder;
    private final MailForgotPassProducer mailForgotPassProducer;
    private final JwtProvider jwtProvider;

    public AuthService(IAuthRepository authRepository, IUserProfileManager userProfileManager, UserRegisterProducer userRegisterProducer, MailSenderProducer mailSenderProducer, UserForgotPassProducer userForgotPassProducer, PasswordEncoder passwordEncoder, MailForgotPassProducer mailForgotPassProducer, JwtProvider jwtProvider) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userProfileManager = userProfileManager;
        this.userRegisterProducer = userRegisterProducer;
        this.mailSenderProducer = mailSenderProducer;
        this.userForgotPassProducer = userForgotPassProducer;
        this.passwordEncoder = passwordEncoder;
        this.mailForgotPassProducer = mailForgotPassProducer;
        this.jwtProvider = jwtProvider;
    }

    @Transactional //rolback --> Bir metodun veya metotları içeren bir sınıfın işlemlerini veritabanı üzerinde otomatik olarak
                               //yönetmek için kullanılır. Yalnızca Post, Put,Delete' de kullanılır.
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        if (auth.getPassword().equals(dto.getRePassword())){
            auth.setActivationCode(CodeGenerator.generateCode());
            authRepository.save(auth);
            //save(auth);
            //39. satırdan sonra auth' un id bilgisi vardır.
            //1. alternatif
            /*UserCreateRequestDto userDto = IAuthMapper.INSTANCE.fromRegisterDtoToUserCreateDto(dto);
            userDto.setAuthId(auth.getId());
            userProfileManager.createUser(userDto);*/

            //2.alternatif
            userProfileManager.createUser(IAuthMapper.INSTANCE.fromRegisterDtoToUserCreateDto(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToAuthRegisterResponseDto(auth);
        return responseDto;
    }

    //TODO maile gönderilen verilerde password şifrelenmiş olarak gönderiliyor.
    public RegisterResponseDto registerWithRabbitMQ(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        if (auth.getPassword().equals(dto.getRePassword())){
            auth.setActivationCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            userRegisterProducer.sendNewUser(IAuthMapper.INSTANCE.fromAuthToUserRegisterModel(auth));
            //rabbit mail sender
            mailSenderProducer.sendNewMail(IAuthMapper.INSTANCE.fromAuthToMailSenderModel(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToAuthRegisterResponseDto(auth);
        return responseDto;
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return jwtProvider.createToken(auth.get().getId(), auth.get().getRole())
                .orElseThrow(() -> {throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    @Transactional
    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(optionalAuth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ALREADY_ACTIVE);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.PENDING)){
            throw new AuthManagerException(ErrorType.USER_ACCESS_ERROR);
        }
        if(dto.getActivationCode().equals(optionalAuth.get().getActivationCode())){
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            //userprofilemanager
            userProfileManager.activateStatus(optionalAuth.get().getId());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    //userprofile --> openfeign
    public Boolean updateAuth(AuthUpdateRequestDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isPresent()){
            save(IAuthMapper.INSTANCE.fromAuthUpdateDtoToAuth(dto, auth.get()));
            return true;
        }
        throw new RuntimeException("Hata");
    }

    @Transactional
    public Boolean delete(String token){
        Optional<Long> authId = jwtProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> auth = authRepository.findById(authId.get());
        //optional nesnesinin aşağıdaki iki kontrolü de kullanılır ve aynıdır
        auth.orElseThrow(() -> {throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        });
        /*if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }*/
        if (auth.get().getStatus().equals(EStatus.ACTIVE) || auth.get().getStatus().equals(EStatus.PENDING)){
            auth.get().setStatus(EStatus.DELETED);
            update(auth.get());
            //userprofilemanager
            userProfileManager.deleteUser(authId.get());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public String forgotPassword(ForgotPasswordRequestDto dto){
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isPresent() && auth.get().getStatus().equals(EStatus.ACTIVE)){
            //random password
            String randomPassword = UUID.randomUUID().toString();
            auth.get().setPassword(passwordEncoder.encode(randomPassword));
            save(auth.get());
            //userprofilemanager
            UserSetPasswordRequestDto userProfileDto = UserSetPasswordRequestDto.builder()
                    .authId(auth.get().getId())
                    .password(auth.get().getPassword())
                    .build();
            userProfileManager.forgotPassword(userProfileDto);
            return "Yeni şifreniz: " + randomPassword;
        }
        throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
    }

    public String forgotPasswordWithRabbitMq(ForgotPasswordRequestDto dto){
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isPresent() && auth.get().getStatus().equals(EStatus.ACTIVE)){
            //random password
            String randomPassword = UUID.randomUUID().toString();
            auth.get().setPassword(passwordEncoder.encode(randomPassword));
            update(auth.get());
            //userprofile rabbitmq
            userForgotPassProducer.userForgotPassword(IAuthMapper.INSTANCE.fromAuthToForgotPassModel(auth.get()));

            //1. yöntem --> builder
            /*mailForgotPassProducer.forgotPassSendMail(MailForgotPassModel.builder()
                            .username(auth.get().getUsername())
                            .email(auth.get().getEmail())
                            .randomPassword(randomPassword)
                    .build());*/
            //2. yöntem --> mapper
            MailForgotPassModel model = IAuthMapper.INSTANCE.fromAuthToMailForgotPassModel(auth.get());
            model.setRandomPassword(randomPassword);
            mailForgotPassProducer.forgotPassSendMail(model);

            return "Yeni şifreniz: " + randomPassword;
        }
        throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
    }


    public Boolean passwordChange(ToAuthPasswordChangeRequestDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        update(auth.get());
        return true;
    }



}















