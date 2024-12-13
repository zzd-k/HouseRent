package com.tao.rental.common;

import lombok.Data;

/**
 * 统一返回的包装类
 */
public class Result {
    private String msg;
    private Object data;
    private String code;

    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("获取成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode("500");
        result.setMsg("系统错误");
        return result;
    }

    public static Result error(String msg,String code) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
