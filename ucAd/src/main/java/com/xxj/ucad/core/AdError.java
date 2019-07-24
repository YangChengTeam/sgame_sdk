package com.xxj.ucad.core;

public class AdError {
    public static final int AD_INIT_ERROR = 1002;
    public static final int AD_ERROR = 1004;

    private String code;
    private String message;
    private Throwable throwable;

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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
