package com.ftp.deploy.config.entity;

import lombok.Data;

/**
 * APP 结果集返回处理类
 *
 * @author ZERO
 */
@Data
public class ResultInfo {
    /**
     * 状态
     * 1 成功
     * 2 失败
     * 3 token失效
     * 4、系统异常
     */
    private Integer status;
    /**
     * 消息个数
     */
    private Integer count;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 数据集合
     */
    private Object data;

    private Object custom;
    public ResultInfo(Integer status, String msg) {
        this(status,msg,null);
    }

    public ResultInfo(Integer status, String msg, Object data) {
        this(status,null,msg,data,null);
    }

    public ResultInfo(Integer status, Integer count, String msg, Object data) {
        this(status,count,msg,data,null);

    }

    public ResultInfo(Integer status, String msg, Object data,Object custom) {
        this(status,null,msg,data,custom);

    }

    public ResultInfo(Integer status, Integer count, Object data) {
        this(status,count,"",data,null);
    }


    public ResultInfo(Integer status, Integer count, String msg, Object data,Object custom) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.count = count;
        this.custom = custom;
    }


    /**
     * 成功的返回结果
     *
     * @return
     */
    public static ResultInfo success() {
        return success("成功");
    }

    public static ResultInfo success(String msg) {
        return success(msg, null);
    }

    public static ResultInfo successData(Object data) {
        return success("成功", data);
    }

    public static ResultInfo success(String msg, Object data) {
        return success(Digit.ONE.getInt(), 0, msg, data);
    }

    public static ResultInfo success(Integer count, Object data) {
        return success(Digit.ONE.getInt(), count, "成功", data);
    }

    public static ResultInfo success(Integer count, String msg, Object data) {
        return success(Digit.ONE.getInt(), count, msg, data);
    }

    public static ResultInfo success(Integer status, Integer count, String msg, Object data) {
        return success(status, count, msg, data,null);
    }

    public static ResultInfo success(String msg, Object data,Object custom) {
        return success(Digit.ONE.getInt(), null, msg, data,custom);
    }

    public static ResultInfo success(Integer status, Integer count, String msg, Object data,Object custom) {
        return new ResultInfo(status, count, msg, data,custom);
    }


    /**
     * 失败的返回结果
     *
     * @return
     */
    public static ResultInfo error() {
        return error("失败");
    }

    public static ResultInfo error(String msg) {
        return error(Digit.TWO.getInt(), msg);
    }

    public static ResultInfo error(Integer status, String msg) {
        return new ResultInfo(status, msg);
    }


}
