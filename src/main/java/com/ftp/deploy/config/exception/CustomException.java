package com.ftp.deploy.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义的异常类
 *
 * @author ZERO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {

    public CustomException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;
}