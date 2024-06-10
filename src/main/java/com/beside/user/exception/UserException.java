package com.beside.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserErrorInfo userErrorInfo;

    public UserException(UserErrorInfo userErrorInfo) {
        super(userErrorInfo.getMessage());
        this.userErrorInfo = userErrorInfo;
    }

}
