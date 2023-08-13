package com.socialmedia.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    //UserProfile Service ERROR
    BAD_REQUEST(4000, "Parametre hatası", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(3000, "Validasyon hatası", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4001, "Kullanıcı adı veya şifre hatalı", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4002, "Bu kullanıcı zaten kayıtlı", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4003,"Böyle bir Kullanıcı Bulunamadı",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4004,"Hesabınız Aktif değil",HttpStatus.BAD_REQUEST),
    ACTIVATE_CODE_ERROR(4005,"Aktivasyon kodu hatası",HttpStatus.BAD_REQUEST),
    ALREADY_ACTIVE(4006,"Hesabınız Zaten Aktif",HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(4007, "Şifreler uyuşmuyor.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4008,"Token hatası",HttpStatus.BAD_REQUEST),

    //SERVER ERROR
    INTERNAL_ERROR(5000,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    HttpStatus httpStatus;
}
