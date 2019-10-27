package com.ftp.deploy.config;

/**
 * 状态
 * @author ZERO
 */
public enum StatusEnum {

    /**
     * 返回值状态
     */
    SUCCESS(1,"成功"),
    FAIL(2,"失败"),
    OFFLINE(3,"账户在别处登录"),
    EXCEPTION(4,"系统异常"),
    NO_PERMISSION(5,"无权限");

    private int code;
    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
