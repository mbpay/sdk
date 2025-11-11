<?php

namespace MBPay\Exception;

/**
 * 错误码常量
 */
class ErrorCode
{
    const SUCCESS = 0;
    const APP_ID_EMPTY = 12000;
    const SIGN_EMPTY = 12001;
    const TIMESTAMP_EMPTY = 12002;
    const MERCHANT_NOT_EXISTS = 12003;
    const SIGN_ERROR = 12005;
    const PARAM_ERROR = 12006;
    const MERCHANT_NOT_EXISTS2 = 12007;
    const MERCHANT_STATUS_ERROR = 12008;
    const MERCHANT_NOT_EXISTS3 = 12009;
    const INSUFFICIENT_BALANCE = 12010;
    const ORDER_EXISTS_OR_ADDR_NOT_FOUND = 12011;
    const SYSTEM_ERROR = 12012;
}





