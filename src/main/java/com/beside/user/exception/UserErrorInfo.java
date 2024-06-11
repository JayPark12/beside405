package com.beside.user.exception;

import lombok.Getter;

@Getter
public enum UserErrorInfo {
    NOT_FOUND_USER("500", "아이디를 찾을 수 없습니다."),
    UNAVAILABLE_USER("500", "사용 불가능한 유저입니다."),
    PASSWORD_MISMATCH("500", "비밀번호가 일치하지 않습니다."),
    NOT_HAVE_PERMISSION("500", "접근 권한이 없습니다."),
    ID_ALREADY_EXISTS("500", "아이디가 이미 존재합니다.");

    private final String code;
    private final String message;

    UserErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
