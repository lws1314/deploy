package com.ftp.deploy.config.entity;

import lombok.Data;

/**
 * 封装layui table 数据信息
 * @author ZERO
 */
@Data
public class TableResult {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 总条数
     */
    private Integer count;
    /**
     * list 集合数据
     */
    private Object data;
}
