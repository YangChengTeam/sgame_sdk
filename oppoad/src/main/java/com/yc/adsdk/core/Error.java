package com.yc.adsdk.core;

public class Error {
    public static final int LOGIN_INIT_ERROR = 1000;
    public static final int AD_INIT_ERROR = 1002;
    public static final int LOGIN_ERROR = 1003;
    public static final int AD_ERROR = 1004;
    public static final int LOGOUT_ERROR = 1005;

    private String code;
    private int tripartiteCode;
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

    public void setTripartiteCode(int tripartiteCode) {
        this.tripartiteCode = tripartiteCode;
    }

    public int getTripartiteCode() {
        return tripartiteCode;
    }
}
