package com.mbpay.model;

import com.mbpay.exception.MBPayException;

import java.util.Map;

/**
 * API 统一返回结构
 */
public class Response {
    private int code;
    private String message;
    private Map<String, Object> data;

    public Response(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 检查响应是否成功
     *
     * @return true 表示成功
     */
    public boolean isSuccess() {
        return this.code == 0;
    }

    /**
     * 将响应转换为异常
     *
     * @return MBPayException
     */
    public MBPayException toException() {
        return new MBPayException(this.code, this.message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }
}






