package com.ftp.deploy.config.exception;

import com.ftp.deploy.config.entity.Digit;
import com.ftp.deploy.config.entity.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * @author ZERO
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResultInfo handlerException(Exception e) {
        ResultInfo resultInfo =null;
        //如果是自定义的异常，返回对应的错误信息
        if (e instanceof CustomException) {
            CustomException exception = (CustomException) e;
            resultInfo = ResultInfo.error(exception.getCode(), exception.getMsg());
            log.error("custom=>"+exception.getStackTrace()[0]+exception.getMsg());
            return resultInfo;
        }else {
            //如果不是已知异常，返回系统异常
            log.error("system=>"+e.getClass().getName(),e);
            resultInfo = ResultInfo.error(Digit.FOUR.getInt(),e.getClass().getName());
            return resultInfo;
        }
    }
}