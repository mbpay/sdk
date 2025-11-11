<?php

namespace MBPay\Types;

/**
 * 支付链接生成请求参数
 */
class PaymentLinkRequest
{
    /**
     * @var string 商户订单号（必填）
     */
    private $orderNo;

    /**
     * @var string 商品描述（必填）
     */
    private $subject;

    /**
     * @var int 订单金额（分，必填）
     */
    private $amount;

    /**
     * @var int 过期时间（分钟，必填）
     */
    private $expire;

    /**
     * @var string|null 随机数（可选，不填会自动生成）
     */
    private $nonce;

    /**
     * @var string|null 回调通知地址（可选）
     */
    private $notifyUrl;

    /**
     * @param string $orderNo 商户订单号（必填）
     * @param string $subject 商品描述（必填）
     * @param int $amount 订单金额（分，必填）
     * @param int $expire 过期时间（分钟，必填）
     * @param string|null $nonce 随机数（可选，不填会自动生成）
     * @param string|null $notifyUrl 回调通知地址（可选）
     */
    public function __construct(
        string $orderNo,
        string $subject,
        int $amount,
        int $expire,
        ?string $nonce = null,
        ?string $notifyUrl = null
    ) {
        $this->orderNo = $orderNo;
        $this->subject = $subject;
        $this->amount = $amount;
        $this->expire = $expire;
        $this->nonce = $nonce;
        $this->notifyUrl = $notifyUrl;
    }

    /**
     * 获取商户订单号
     *
     * @return string
     */
    public function getOrderNo(): string
    {
        return $this->orderNo;
    }

    /**
     * 获取商品描述
     *
     * @return string
     */
    public function getSubject(): string
    {
        return $this->subject;
    }

    /**
     * 获取订单金额
     *
     * @return int
     */
    public function getAmount(): int
    {
        return $this->amount;
    }

    /**
     * 获取过期时间
     *
     * @return int
     */
    public function getExpire(): int
    {
        return $this->expire;
    }

    /**
     * 获取随机数
     *
     * @return string|null
     */
    public function getNonce(): ?string
    {
        return $this->nonce;
    }

    /**
     * 获取回调通知地址
     *
     * @return string|null
     */
    public function getNotifyUrl(): ?string
    {
        return $this->notifyUrl;
    }
}





