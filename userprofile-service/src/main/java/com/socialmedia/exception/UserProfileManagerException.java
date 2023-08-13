package com.socialmedia.exception;

import lombok.Getter;

@Getter
public class UserProfileManagerException extends RuntimeException{

    private final ErrorType errorType;

    public UserProfileManagerException(ErrorType errorType) {
        this.errorType = errorType;
    }
    public UserProfileManagerException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }
}
