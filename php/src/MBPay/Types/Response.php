<?php

namespace MBPay\Types;

use MBPay\Exception\MBPayException;

/**
 * API 统一返回结构
 */
class Response
{
    /**
     * @var int 0 表示成功，非0为错误码
     */
    private $code;

    /**
     * @var string 错误描述信息
     */
    private $message;

    /**
     * @var array 返回数据
     */
    private $data;

    /**
     * @param int $code
     * @param string $message
     * @param array $data
     */
    public function __construct(int $code, string $message, array $data)
    {
        $this->code = $code;
        $this->message = $message;
        $this->data = $data;
    }

    /**
     * 检查响应是否成功
     *
     * @return bool
     */
    public function isSuccess(): bool
    {
        return $this->code === 0;
    }

    /**
     * 将响应转换为异常
     *
     * @return MBPayException
     */
    public function toException(): MBPayException
    {
        return new MBPayException($this->code, $this->message);
    }

    /**
     * 获取错误码
     *
     * @return int
     */
    public function getCode(): int
    {
        return $this->code;
    }

    /**
     * 获取错误信息
     *
     * @return string
     */
    public function getMessage(): string
    {
        return $this->message;
    }

    /**
     * 获取返回数据
     *
     * @return array
     */
    public function getData(): array
    {
        return $this->data;
    }
}





