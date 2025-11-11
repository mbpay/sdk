<?php

namespace MBPay\Exception;

use Exception;

/**
 * MBPay API 自定义异常
 */
class MBPayException extends Exception
{
    /**
     * @var int 错误码
     */
    private $errorCode;

    /**
     * @param int $code 错误码
     * @param string $message 错误信息
     */
    public function __construct(int $code, string $message)
    {
        $this->errorCode = $code;
        parent::__construct("MBPay API Error [{$code}]: {$message}", $code);
    }

    /**
     * 获取错误码
     *
     * @return int
     */
    public function getErrorCode(): int
    {
        return $this->errorCode;
    }
}





