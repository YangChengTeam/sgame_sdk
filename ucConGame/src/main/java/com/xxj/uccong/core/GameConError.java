package com.xxj.uccong.core;

public class GameConError {
    public static final int LOGIN_ERROR = 1003;
    public static final int AD_ERROR = 1004;
    public static final int LOGOUT_ERROR = 1005;

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
