package com.mbpay.exception;

/**
 * MBPay API 自定义异常
 */
public class MBPayException extends Exception {
    private int errorCode;

    public MBPayException(int code, String message) {
        super("MBPay API Error [" + code + "]: " + message);
        this.errorCode = code;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public int getErrorCode() {
        return errorCode;
    }
}






