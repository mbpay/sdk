/**
 * 错误处理
 */

/**
 * 错误码常量
 */
export enum ErrorCode {
    SUCCESS = 0,
    APP_ID_EMPTY = 12000,
    SIGN_EMPTY = 12001,
    TIMESTAMP_EMPTY = 12002,
    MERCHANT_NOT_EXISTS = 12003,
    SIGN_ERROR = 12005,
    PARAM_ERROR = 12006,
    MERCHANT_NOT_EXISTS2 = 12007,
    MERCHANT_STATUS_ERROR = 12008,
    MERCHANT_NOT_EXISTS3 = 12009,
    INSUFFICIENT_BALANCE = 12010,
    ORDER_EXISTS_OR_ADDR_NOT_FOUND = 12011,
    SYSTEM_ERROR = 12012,
}

/**
 * MBPay API 自定义错误
 */
export class MBPayError extends Error {
    private errorCode: number;

    constructor(code: number, message: string) {
        super(`MBPay API Error [${code}]: ${message}`);
        this.errorCode = code;
        this.name = 'MBPayError';
    }

    /**
     * 获取错误码
     */
    getErrorCode(): number {
        return this.errorCode;
    }
}





